package com.example.jsonplaceholderposts.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jsonplaceholderposts.data.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>)

    @Query("SELECT * FROM comment")
    fun loadComments(): LiveData<List<Comment>>
}