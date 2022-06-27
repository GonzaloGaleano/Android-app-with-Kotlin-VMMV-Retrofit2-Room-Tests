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
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.databinding.ActivityPostListBinding
import com.example.jsonplaceholderposts.ui.post.show.PostActivity


class PostListActivity : AppCompatActivity(), PostListAdapter.OnItemListener {
    private var loadingPosts: Boolean = false
    private var loadingComments: Boolean = false
    private var loadingUsers: Boolean = false
    private var posts: List<Post>? = null
    private var comments: List<Comment>? = null
    private var users: List<User>? = null
    private lateinit var adapter: PostListAdapter
    private val viewModel: PostListViewModel by viewModels()
    private lateinit var binding: ActivityPostListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        prepareRecyclerView(binding.postsRecyclerView, listOf())
        observerPost()
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
            this.posts = loadPostsData(posts)
        }
        viewModel.comments.observe(this) { comments ->
            this.comments = comments
            posts?.let {
                loadPostsData(it)
            }
        }
        viewModel.users.observe(this) { users ->
            this.users = users
            posts?.let {
                loadPostsData(it)
            }
        }
    }

    private fun displayLoading() {
        binding.progressBar.visibility = if (loadingPosts || loadingUsers || loadingComments) View.VISIBLE else View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostsData(posts: List<Post>): List<Post> {
        users?.let {
            posts.forEach{ post ->
                post.user = it.first{ it.id == post.userId}
            }
        }
        comments?.let {
            posts.forEach{ post ->
                post.comments = it.filter{ it.postId == post.id}
            }
        }
        if (adapter.posts != posts) {
            adapter.posts = posts
            adapter.notifyDataSetChanged()
        }
        return posts
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

    private fun prepareRecyclerView(recyclerView: RecyclerView, posts: List<Post>) {
        adapter = PostListAdapter(posts)
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