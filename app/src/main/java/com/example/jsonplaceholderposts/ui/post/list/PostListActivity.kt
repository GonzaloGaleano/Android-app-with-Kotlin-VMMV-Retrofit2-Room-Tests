package com.example.jsonplaceholderposts.ui.post.list

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.R
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.databinding.ActivityPostListBinding
import com.example.jsonplaceholderposts.repository.PostRepository
import com.example.jsonplaceholderposts.ui.post.show.PostActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostListActivity : AppCompatActivity(), PostListAdapter.OnItemListener {
    private var deleting: Boolean = false
    private var filterFavorites: Boolean = false
    private var loadingPosts: Boolean = true
    private var loadingComments: Boolean = true
    private var loadingUsers: Boolean = true
    private var favorites: List<Favorite> = listOf()
    private var posts: MutableList<Post> = mutableListOf()
    private var comments: List<Comment> = listOf()
    private var users: List<User> = listOf()
    private lateinit var adapter: PostListAdapter
    private val viewModel: PostListViewModel by viewModels()
    private lateinit var binding: ActivityPostListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply{
            setKeepOnScreenCondition{
                (loadingPosts || loadingComments || loadingUsers)
            }
        }
        binding = ActivityPostListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        prepareRecyclerView(binding.postsRecyclerView)
        observerPost()
        loadBtnActions()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadBtnActions() {
        binding.btnFavoritesPosts.setOnClickListener {
            if (!deleting) {
                filterFavorites = true
                loadPostsData()
            }
        }
        binding.btnAllPosts.setOnClickListener {
            if (!deleting) {
                filterFavorites = false
                loadPostsData()
            }
        }
        binding.btnDeleteAll.setOnClickListener {
            if (loadingPosts || loadingComments || loadingUsers) {
                Toast.makeText(
                    this,
                    "loading data in progress",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            deleting = true
            displayLoading()
            val totalPostsToDelete = posts.size
            var totalDeleted = 0
            posts.forEach{ post ->
                PostRepository.postsService.deletePost(post.id).apply {
                    enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            println("#${post.id} deleted")
                            totalDeleted++
                            post.comments.forEach { comment ->
                                Thread{
                                    PostRepository.commentDao?.deleteComment(comment)
                                }.start()
                            }
                            /*post.user?.let { user ->
                                Thread{
                                    PostsRepository.userDao?.deleteUser(user)
                                }.start()
                            }*/
                            Thread {
                                PostRepository.favoriteDao?.deleteFavorite(Favorite(post.id))
                                PostRepository.postDao?.deletePost(post)
                            }.start()
                            if (totalDeleted == totalPostsToDelete) {
                                deleting = false
                                displayLoading()
//                                viewModel.loadPosts()
                                adapter.posts = mutableListOf()
                                adapter.notifyDataSetChanged()
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(this@PostListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun observerPost() {
        viewModel.loadingPostList.observe(this) { loadingPosts ->
            this.loadingPosts = loadingPosts
            displayLoading()
        }
        viewModel.loadingComments.observe(this) { loadingComments ->
            this.loadingComments = loadingComments
            displayLoading()
        }
        viewModel.loadingUsers.observe(this) { loadingUsers ->
            this.loadingUsers = loadingUsers
            displayLoading()
        }
        viewModel.posts.observe(this) { posts ->
            val nPosts: MutableList<Post> = posts.ifEmpty { mutableListOf() } as MutableList<Post>
            adapter.posts =  nPosts
            this.posts = nPosts
            loadPostsData()
        }
        viewModel.favorites.observe(this) { favorites ->
            this.favorites = favorites
            loadPostsData()
        }
        viewModel.comments.observe(this) { comments ->
            this.comments = comments
            loadPostsData()
        }
        viewModel.users.observe(this) { users ->
            this.users = users
            loadPostsData()
        }
    }

    private fun displayLoading() {
        binding.progressBar.visibility = if (loadingPosts || loadingUsers || loadingComments || deleting) View.VISIBLE else View.GONE
        binding.btnDeleteAll.isEnabled = !(loadingPosts || loadingUsers || loadingComments || deleting)
        binding.btnDeleteAll.alpha = if (loadingPosts || loadingUsers || loadingComments || deleting) .5F else 1F
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostsData() {
        if (!deleting) {
            val filteredPost = if (filterFavorites) posts.filter { it.favorite } else posts
            filteredPost.forEach{ post ->
                post.favorite = favorites.contains(Favorite(postId = post.id)) == true
                post.user = users.firstOrNull{ it.id == post.userId}
                post.comments = comments.filter{ it.postId == post.id}
            }

            binding.btnDeleteAll.isEnabled = filteredPost.isNotEmpty()
            binding.btnDeleteAll.alpha = if (filteredPost.isEmpty()) .5F else 1F

            adapter.posts = if (filteredPost.isEmpty()) mutableListOf() else filteredPost as MutableList<Post>
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnRefresh -> {
                if (!deleting) {
                    println("refresh...")
                    viewModel.loadPosts()
                }
            }
        }
        return true
    }

    private fun prepareRecyclerView(recyclerView: RecyclerView) {
        adapter = PostListAdapter(mutableListOf())
        adapter.setOnItemListener(this)
        recyclerView.adapter = adapter
    }

    override fun onPost(post: Post) {
        if (!deleting) {
            println("onPost() #${post.id}")
            Intent(this, PostActivity::class.java).apply {
                putExtra(PostActivity.ARG_POST, post)
                startActivity(this)
            }
        }
    }

}