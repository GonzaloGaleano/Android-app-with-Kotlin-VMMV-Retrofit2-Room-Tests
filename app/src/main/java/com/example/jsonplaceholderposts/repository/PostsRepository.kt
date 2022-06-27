package com.example.jsonplaceholderposts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.jsonplaceholderposts.PostApplication
import com.example.jsonplaceholderposts.api.JSONPlaceholderDBService
import com.example.jsonplaceholderposts.api.RetrofitServiceBuilder
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User
import com.example.jsonplaceholderposts.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.random.Random

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

object PostsRepository {
    private var databse: PostsDatabase? = null
    var postDao: PostDao? = null
    private var commentDao: CommentDao? = null
    private var userDao: UserDao? = null
    var favoriteDao: FavoriteDao? = null
    private var retried: Boolean = false

    private val LOADING_POSTS = MutableLiveData<Boolean>()
    val loadingPostList: LiveData<Boolean> get() = LOADING_POSTS

    private val LOADING_COMMENTS = MutableLiveData<Boolean>()
    val loadingComments: LiveData<Boolean> get() = LOADING_COMMENTS

    private val LOADING_USERS = MutableLiveData<Boolean>()
    val loadingUsers: LiveData<Boolean> get() = LOADING_USERS

    private val postsService: JSONPlaceholderDBService = RetrofitServiceBuilder(BASE_URL)
        .buildService(JSONPlaceholderDBService::class.java)

    init {
        PostApplication.appContext().let { context ->
            databse = Room.databaseBuilder(
                context,
                PostsDatabase::class.java,
                "posts-db"
            ).build()
            postDao = databse?.postDao()
            commentDao = databse?.commentDao()
            userDao = databse?.userDao()
            favoriteDao = databse?.favoriteDao()
        }
    }

    fun refreshData() {
        getPostList()
        getComments()
        getUsers()
    }

    fun getPostList(): LiveData<List<Post>> {
        LOADING_POSTS.value = true
        postsService.getPosts().apply {
            enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            Thread{
                                val favorites = favoriteDao?.getFavorites()
                                posts.forEach{ post ->
                                    post.favorite = favorites?.contains(Favorite(postId = post.id)) == true
                                }
                                postDao?.insertPosts(posts)
                            }.start()
                        }
                    }
                    LOADING_POSTS.value = false
                }
                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    LOADING_POSTS.value = false
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getPostList()
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getPostList()
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
                        }
                    }
                }
            })
        }
        return postDao?.loadPosts() ?: MutableLiveData()
    }

    fun getFavorites(): LiveData<List<Favorite>> {
        return favoriteDao?.favoritesLive() ?: MutableLiveData()
    }

    fun getUsers(): LiveData<List<User>> {
        LOADING_USERS.value = true
        postsService.getUsers().apply {
            enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { users ->
                            Thread{
                                userDao?.insertUsers(users)
                            }.start()
                        }
                    }
                    LOADING_USERS.value = false
                }
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    LOADING_USERS.value = false
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getUsers()
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getUsers()
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
                        }
                    }
                }
            })
        }
        return userDao?.loadUsers() ?: MutableLiveData()
    }

    fun getComments(): LiveData<List<Comment>> {
        LOADING_COMMENTS.value = true
        postsService.getComments().apply {
            enqueue(object : Callback<List<Comment>> {
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { comments ->
                            Thread{
                                commentDao?.insertComments(comments)
                            }.start()
                            LOADING_POSTS.value = false
                        }
                    }
                    LOADING_COMMENTS.value = false
                }
                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    LOADING_COMMENTS.value = false
                    when {
                        t is SocketTimeoutException && !retried -> {
                            retried = !retried
                            getComments()
                        }
                        t is IOException && !retried -> {
                            retried = !retried
                            getComments()
                        }
                        else -> {
                            retried = !retried
                            LOADING_POSTS.value = false
                            when {
                                call.isCanceled -> {
                                    println("Call was cancelled forcefully")
                                }
                                else -> {
                                    println("Network Error :: " + t.localizedMessage)
                                }
                            }
                        }
                    }
                }
            })
        }
        return commentDao?.loadComments() ?: MutableLiveData()
    }
}