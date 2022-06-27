package com.example.jsonplaceholderposts.ui.post.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jsonplaceholderposts.repository.PostRepository
import com.example.jsonplaceholderposts.repository.ThePostDBRepository

class PostListViewModel(private val repository: PostRepository): ViewModel() {

    val posts = repository.getPostList()
    val comments = repository.getComments()
    val users = repository.getUsers()
    val favorites = repository.getFavorites()
    val loadingPostList = repository.loadingPostList
    val loadingUsers = repository.loadingUsers
    val loadingComments = repository.loadingComments

    fun loadPosts() {
        repository.refreshData()
    }

}

class PostLIstViewModelFactory(private val repository: PostRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(repository) as T
    }

}