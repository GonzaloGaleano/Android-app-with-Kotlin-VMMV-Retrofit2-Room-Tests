package com.example.jsonplaceholderposts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jsonplaceholderposts.data.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>)

    @Delete
    fun deleteComment(comment: Comment)

    @Query("SELECT * FROM comment")
    fun loadComments(): LiveData<List<Comment>>
}