package com.example.jsonplaceholderposts.ui.post.list

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
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.databinding.ActivityPostListBinding
import com.example.jsonplaceholderposts.ui.post.show.PostActivity


class PostListActivity : AppCompatActivity(), PostListAdapter.OnItemListener {
    private lateinit var adapter: PostListAdapter
    private val viewModel: PostListViewModel by viewModels()
    private lateinit var binding: ActivityPostListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        observerPost()
    }

    private fun observerPost() {
        viewModel.loadingPostList.observe(this) { loadingPosts ->
            binding.progressBar.visibility = if (loadingPosts) View.VISIBLE else View.GONE
        }
        viewModel.posts.observe(this) { posts ->
            prepareRecyclerView(binding.postsRecyclerView, posts)
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