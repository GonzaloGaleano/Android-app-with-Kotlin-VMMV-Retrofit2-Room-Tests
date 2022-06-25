package com.example.jsonplaceholderposts.ui.post.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.databinding.ActivityPostsBinding


class PostsActivity : AppCompatActivity() {
    private val viewModel: PostListViewModel by viewModels()
    private lateinit var binding: ActivityPostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        observerPost()
    }

    private fun observerPost() {
        viewModel.posts.observe(this) { posts ->
            prepareRecyclerView(binding.postsRecyclerView, posts)
        }
    }

    private fun prepareRecyclerView(recyclerView: RecyclerView, posts: List<Post>) {
        recyclerView.adapter = PostListAdapter(posts)
    }
}