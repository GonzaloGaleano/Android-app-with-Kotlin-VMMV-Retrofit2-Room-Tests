package com.example.jsonplaceholderposts.ui.post.list

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.R
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.databinding.ActivityPostListBinding
import com.example.jsonplaceholderposts.ui.post.show.PostActivity


class PostListActivity : AppCompatActivity(), PostListAdapter.OnItemListener {
    private var filterFavorites: Boolean = false
    private var loadingPosts: Boolean = false
    private var loadingComments: Boolean = false
    private var loadingUsers: Boolean = false
    private var favorites: List<Favorite> = listOf()
    private var posts: MutableList<Post> = mutableListOf()
    private var comments: List<Comment> = listOf()
    private var users: List<User> = listOf()
    private lateinit var adapter: PostListAdapter
    private val viewModel: PostListViewModel by viewModels()
    private lateinit var binding: ActivityPostListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        prepareRecyclerView(binding.postsRecyclerView)
        observerPost()
        loadBtnActions()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadBtnActions() {
        binding.btnFavoritesPosts.setOnClickListener {
            filterFavorites = true
            loadPostsData()
        }
        binding.btnAllPosts.setOnClickListener {
            filterFavorites = false
            loadPostsData()
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
        binding.progressBar.visibility = if (loadingPosts || loadingUsers || loadingComments) View.VISIBLE else View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostsData() {
        val filteredPost = if (filterFavorites) posts.filter { it.favorite } else posts
        filteredPost.forEach{ post ->
            post.favorite = favorites.contains(Favorite(postId = post.id)) == true
            post.user = users.first{ it.id == post.userId}
            post.comments = comments.filter{ it.postId == post.id}
        }

        if (adapter.posts.size != filteredPost.size) {
            adapter.posts = if (filteredPost.isEmpty()) mutableListOf() else filteredPost as MutableList<Post>
            adapter.notifyDataSetChanged()
        } else {
            for (i in 1 until filteredPost.size) {
                adapter.posts[i] = filteredPost[i]
                adapter.notifyItemChanged(i)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnRefresh -> {
                println("refresh...")
                viewModel.loadPosts()
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
        println("onPost() #${post.id}")
        Intent(this, PostActivity::class.java).apply {
            putExtra(PostActivity.ARG_POST, post)
            startActivity(this)
        }
    }

}