package com.example.jsonplaceholderposts.ui.post.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.databinding.PostRowBinding

class PostListAdapter(var posts: MutableList<Post>): RecyclerView.Adapter<PostListAdapter.PostListViewHolder>()  {
    private lateinit var mListener: OnItemListener
    inner class PostListViewHolder(val binding: PostRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListViewHolder {
        return PostListViewHolder(PostRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        val post: Post = posts[position]
        holder.apply {
            binding.starFav.isVisible = post.favorite
            binding.starFavNo.isVisible = !post.favorite
            binding.textViewTitle.text = post.title
            binding.btnPost.setOnClickListener {
                mListener.onPost(post)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    fun setOnItemListener(mListener: OnItemListener) {
        this.mListener = mListener
    }

    interface OnItemListener {
        fun onPost(post: Post)
    }
}