package com.example.jsonplaceholderposts.ui.post.show

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.jsonplaceholderposts.R
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.databinding.ActivityPostBinding
import com.google.android.material.appbar.CollapsingToolbarLayout

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private var post: Post? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(LayoutInflater.from(this))
        intent.let {
            post = it.getSerializableExtra(ARG_POST) as Post?
            binding.post = post
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnAddToFav -> {
                println("add to fav")
            }
        }
        return true
    }

    companion object {
        const val ARG_POST = "post"
    }
}