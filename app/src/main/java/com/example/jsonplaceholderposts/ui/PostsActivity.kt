package com.example.jsonplaceholderposts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.jsonplaceholderposts.R


class PostsActivity : AppCompatActivity() {
    private val viewModel: PostListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        observerPost()
    }

    private fun observerPost() {
        viewModel.posts.observe(this) { posts ->
            println(posts)
        }
    }
}