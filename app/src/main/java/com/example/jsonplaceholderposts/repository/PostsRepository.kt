package com.example.jsonplaceholderposts.repository

import com.example.jsonplaceholderposts.api.JSONPlaceholderDBService
import com.example.jsonplaceholderposts.api.RetrofitServiceBuilder
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.random.Random

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

object PostsRepository {
    private var retried: Boolean = false
    private val postsService: JSONPlaceholderDBService = RetrofitServiceBuilder(BASE_URL)
        .buildService(JSONPlaceholderDBService::class.java)

    fun getPostList(onPosts: (List<Post>) -> Unit) {
        postsService.getPosts().apply {
            enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            getFavorites(posts, onPosts)
                        }
                    }
                }
                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getPostList(onPosts)
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getPostList(onPosts)
                        }
                        else -> {
                            retried = !retried
                            when {
                                call.isCanceled -> {
                                    println("Call was cancelled forcefully")
                                }
                                else -> {
                                    println("Network Error :: " + t.localizedMessage)
                                }
                            }
                            onPosts(emptyList())
                        }
                    }
                }
            })
        }
    }

    fun getFavorites(posts: List<Post>, onPosts: (List<Post>) -> Unit) {
        posts.forEach { post ->
            post.favorite = Random.nextBoolean()
        }
        getUsers(posts, onPosts)
    }

    fun getUsers(posts: List<Post>, onPosts: (List<Post>) -> Unit) {
        postsService.getUsers().apply {
            enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { users ->
                            posts.forEach { post ->
                                post.user = users.first { user -> post.userId == user.id}
                            }
                            getComments(posts, onPosts)
                        }
                    }
                }
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getUsers(posts, onPosts)
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getUsers(posts, onPosts)
                        }
                        else -> {
                            retried = !retried
                            when {
                                call.isCanceled -> {
                                    println("Call was cancelled forcefully")
                                }
                                else -> {
                                    println("Network Error :: " + t.localizedMessage)
                                }
                            }
                            onPosts(emptyList())
                        }
                    }
                }
            })
        }
    }

    fun getComments(posts: List<Post>, onPosts: (List<Post>) -> Unit) {
        postsService.getComments().apply {
            enqueue(object : Callback<List<Comment>> {
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { comments ->
                            posts.forEach { post ->
                                post.comments = comments.filter { comment -> comment.postId == post.id }
                            }
                            onPosts(posts)
                        }
                    }
                }
                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getComments(posts, onPosts)
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getComments(posts, onPosts)
                        }
                        else -> {
                            retried = !retried
                            when {
                                call.isCanceled -> {
                                    println("Call was cancelled forcefully")
                                }
                                else -> {
                                    println("Network Error :: " + t.localizedMessage)
                                }
                            }
                            onPosts(emptyList())
                        }
                    }
                }
            })
        }
    }
}