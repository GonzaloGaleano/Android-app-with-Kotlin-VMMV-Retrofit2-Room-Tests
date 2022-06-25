package com.example.jsonplaceholderposts.api

import com.example.jsonplaceholderposts.data.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JSONPlaceholderDBService {
    @GET("posts")
    fun getPosts(): Call<Post>

    @GET("posts/{postId}")
    fun getPost(
        @Path("postId") postId: String,
    ): Call<Post>
}