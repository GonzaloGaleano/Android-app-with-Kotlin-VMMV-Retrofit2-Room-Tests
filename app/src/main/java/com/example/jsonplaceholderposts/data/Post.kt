package com.example.jsonplaceholderposts.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Post(
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
): Serializable {
    var favorite: Boolean = false
    @Ignore
    var user: User? = null
    @Ignore
    var comments: List<Comment> = mutableListOf()
}