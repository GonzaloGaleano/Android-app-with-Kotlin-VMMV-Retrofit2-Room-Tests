package com.example.jsonplaceholderposts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
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
            println(posts)
        }
    }
}