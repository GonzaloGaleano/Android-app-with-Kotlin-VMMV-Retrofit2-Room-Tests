package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.repository.PostsRepository

class PostListViewModel: ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    init {
        PostsRepository.getPost { posts ->
            _posts.value = posts
        }
    }

}