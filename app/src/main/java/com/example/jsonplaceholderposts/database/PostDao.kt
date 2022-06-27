package com.example.jsonplaceholderposts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jsonplaceholderposts.data.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)

    @Update
    fun updatePost(post: Post)

    @Delete
    fun deletePost(post: Post)

//    @Query("SELECT * FROM post p, comment c, user u WHERE p.userId = u.id AND c.postId = p.id")
    @Query("SELECT * FROM post")
    fun loadPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM post WHERE favorite")
    fun loadFavoritesPosts(): List<Post>
}