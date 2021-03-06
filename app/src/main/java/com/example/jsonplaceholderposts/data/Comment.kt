package com.example.jsonplaceholderposts.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Comment(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
    val postId: Int,
): Serializable
