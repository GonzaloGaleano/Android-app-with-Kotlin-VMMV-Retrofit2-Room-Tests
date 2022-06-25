package com.example.jsonplaceholderposts.repository

import com.example.jsonplaceholderposts.api.JSONPlaceholderDBService
import com.example.jsonplaceholderposts.api.RetrofitServiceBuilder
import com.example.jsonplaceholderposts.data.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

object PostsRepository {
    private val postsService: JSONPlaceholderDBService = RetrofitServiceBuilder(BASE_URL)
        .buildService(JSONPlaceholderDBService::class.java)

    fun getPost(onPosts: (List<Post>) -> Unit) {
        postsService.getPosts().apply {
            enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            onPosts(posts)
                        }
                    }
                }
                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    onPosts(emptyList())
                }
            })
        }
    }
}