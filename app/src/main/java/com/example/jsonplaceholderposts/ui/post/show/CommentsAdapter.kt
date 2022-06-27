package com.example.jsonplaceholderposts.ui.post.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.databinding.CommentRowBinding

class CommentsAdapter( private val comments: List<Comment>): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    inner class CommentsViewHolder(val binding: CommentRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(CommentRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = comments[position]
        holder.apply {
            binding.comment = comment
        }
    }

    override fun getItemCount(): Int = comments.size
}