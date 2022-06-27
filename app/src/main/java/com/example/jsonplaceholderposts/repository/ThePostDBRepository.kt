package com.example.jsonplaceholderposts.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.jsonplaceholderposts.PostApplication
import com.example.jsonplaceholderposts.api.Env
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

object ThePostDBRepository:
    PostRepository {

    val postsService =
            RetrofitServiceBuilder(Env.BASE_URL).buildService(JSONPlaceholderDBService::class.java)

    private var databse: PostsDatabase? = null
    var postDao: PostDao? = null
    var commentDao: CommentDao? = null
    var userDao: UserDao? = null
    var favoriteDao: FavoriteDao? = null
    private var retried: Boolean = false

    private val mLoadingPostList = MutableLiveData<Boolean>()
    override val loadingPostList: LiveData<Boolean> get() = mLoadingPostList

    private val mLoadingComments = MutableLiveData<Boolean>()
    override val loadingComments: LiveData<Boolean> get() = mLoadingComments

    private val mLoadingUsers = MutableLiveData<Boolean>()
    override val loadingUsers: LiveData<Boolean> get() = mLoadingUsers

    init {
        databse = Room.databaseBuilder(
            PostApplication.appContext(),
            PostsDatabase::class.java,
            "posts-db"
        ).build()
        postDao = databse?.postDao()
        commentDao = databse?.commentDao()
        userDao = databse?.userDao()
        favoriteDao = databse?.favoriteDao()
    }

    override fun refreshData() {
        getPostList()
        getComments()
        getUsers()
    }

    override fun getPostList(): LiveData<List<Post>> {
        mLoadingPostList.value = true
        postsService.getPosts().apply {
            enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { posts ->
                            Thread {
                                val favorites = favoriteDao?.getFavorites()
                                posts.forEach { post ->
                                    post.favorite =
                                        favorites?.contains(Favorite(postId = post.id)) == true
                                }
                                postDao?.insertPosts(posts)
                            }.start()
                        }
                    }
                    mLoadingPostList.value = false
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    mLoadingPostList.value = false
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

    override fun getFavorites(): LiveData<List<Favorite>> {
        return favoriteDao?.favoritesLive() ?: MutableLiveData()
    }

    override fun getUsers(): LiveData<List<User>> {
        mLoadingUsers.value = true
        postsService.getUsers().apply {
            enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { users ->
                            Thread {
                                userDao?.insertUsers(users)
                            }.start()
                        }
                    }
                    mLoadingUsers.value = false
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    mLoadingUsers.value = false
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

    override fun getComments(): LiveData<List<Comment>> {
        mLoadingComments.value = true
        postsService.getComments().apply {
            enqueue(object : Callback<List<Comment>> {
                override fun onResponse(
                    call: Call<List<Comment>>,
                    response: Response<List<Comment>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { comments ->
                            Thread {
                                commentDao?.insertComments(comments)
                            }.start()
                            mLoadingPostList.value = false
                        }
                    }
                    mLoadingComments.value = false
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    mLoadingComments.value = false
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
                            mLoadingPostList.value = false
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