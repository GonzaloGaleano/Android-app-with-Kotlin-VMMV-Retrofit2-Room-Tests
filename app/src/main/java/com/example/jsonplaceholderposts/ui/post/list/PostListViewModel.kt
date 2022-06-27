package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.repository.PostsRepository

class PostListViewModel: ViewModel() {

    val posts = PostsRepository.getPostList()
    val comments = PostsRepository.getComments()
    val users = PostsRepository.getUsers()
    val favorites = PostsRepository.getFavorites()
    val loadingPostList = PostsRepository.loadingPostList
    val loadingUsers = PostsRepository.loadingUsers
    val loadingComments = PostsRepository.loadingComments

    fun loadPosts() {
        PostsRepository.refreshData()
    }

}