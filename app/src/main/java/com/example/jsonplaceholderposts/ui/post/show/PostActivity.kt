package com.example.jsonplaceholderposts.ui.post.show

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.R
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.databinding.ActivityPostBinding
import com.example.jsonplaceholderposts.repository.ThePostDBRepository

class PostActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    private lateinit var binding: ActivityPostBinding
    private lateinit var post: Post
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        intent.let {
            (it.getSerializableExtra(ARG_POST) as Post?)?.let{ post ->
                this.post = post
                binding.post = post
                prepareRecyclerView(binding.commentsRecyclerView, post.comments)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_post, menu)
        menuDisplayConfig()
        return true
    }

    private fun menuDisplayConfig() {
        if (post.favorite) {
            hideOption(R.id.btnAddToFav)
            showOption(R.id.btnRemoveFromFav)
        } else {
            showOption(R.id.btnAddToFav)
            hideOption(R.id.btnRemoveFromFav)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnAddToFav -> {
                println("add to fav")
                post.favorite = true
                Thread{
                    ThePostDBRepository.favoriteDao?.insertFavorite(Favorite(postId = post.id))
                    ThePostDBRepository.postDao?.updatePost(post)
                }.start()
                menuDisplayConfig()
            }
            R.id.btnRemoveFromFav -> {
                println("remove from favs")
                post.favorite = false
                Thread{
                    ThePostDBRepository.favoriteDao?.deleteFavorite(Favorite(postId = post.id))
                    ThePostDBRepository.postDao?.updatePost(post)
                }.start()
                menuDisplayConfig()
            }
            R.id.btnDeletePost -> {
                println("deleting post")
                Thread{
                    ThePostDBRepository.favoriteDao?.deleteFavorite(Favorite(post.id))
                    post.comments.forEach { comment ->
                        ThePostDBRepository.commentDao?.deleteComment(comment)
                    }
                    ThePostDBRepository.postDao?.deletePost(post)
                }.start()
                finish()
            }
        }
        return true
    }

    private fun prepareRecyclerView(recyclerView: RecyclerView, comments: List<Comment>) {
        recyclerView.adapter = CommentsAdapter(comments)
    }

    private fun hideOption(id: Int) {
        if (::menu.isInitialized) {
            menu.findItem(id)?.let { item ->
                item.isVisible = false
            }
        }
    }

    private fun showOption(id: Int) {
        if (::menu.isInitialized) {
            menu.findItem(id)?.let { item ->
                item.isVisible = true
            }
        }
    }

    companion object {
        const val ARG_POST = "post"
    }
}