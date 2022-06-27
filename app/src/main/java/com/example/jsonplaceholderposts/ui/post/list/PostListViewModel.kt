package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.ViewModel
import com.example.jsonplaceholderposts.repository.PostRepository

class PostListViewModel: ViewModel() {

    val posts = PostRepository.getPostList()
    val comments = PostRepository.getComments()
    val users = PostRepository.getUsers()
    val favorites = PostRepository.getFavorites()
    val loadingPostList = PostRepository.loadingPostList
    val loadingUsers = PostRepository.loadingUsers
    val loadingComments = PostRepository.loadingComments

    fun loadPosts() {
        PostRepository.refreshData()
    }

}