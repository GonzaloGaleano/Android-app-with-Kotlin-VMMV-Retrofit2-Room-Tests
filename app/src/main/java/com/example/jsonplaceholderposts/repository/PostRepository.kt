package com.example.jsonplaceholderposts.repository

import androidx.lifecycle.LiveData
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User

interface PostRepository {
    val loadingPostList: LiveData<Boolean>
    val loadingComments: LiveData<Boolean>
    val loadingUsers: LiveData<Boolean>
    fun refreshData()
    fun getPostList(): LiveData<List<Post>>
    fun getFavorites(): LiveData<List<Favorite>>
    fun getUsers(): LiveData<List<User>>
    fun getComments(): LiveData<List<Comment>>
}