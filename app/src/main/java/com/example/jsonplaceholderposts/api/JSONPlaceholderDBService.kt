package com.example.jsonplaceholderposts.api

import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JSONPlaceholderDBService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("comments")
    fun getComments(): Call<List<Comment>>

}