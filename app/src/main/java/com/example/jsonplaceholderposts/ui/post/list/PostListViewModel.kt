package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.repository.PostsRepository

class PostListViewModel: ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    private val _loadingPostList = MutableLiveData<Boolean>()
    val loadingPostList: LiveData<Boolean> get() = _loadingPostList

    init {
        loadPosts()
    }

    fun loadPosts() {
        _loadingPostList.value = true
        PostsRepository.getPost { posts ->
            _posts.value = posts
            _loadingPostList.value = false
        }
    }

}