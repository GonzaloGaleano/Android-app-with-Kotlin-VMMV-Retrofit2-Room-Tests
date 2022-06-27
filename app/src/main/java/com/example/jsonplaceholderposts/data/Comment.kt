package com.example.jsonplaceholderposts.data

import java.io.Serializable

data class Comment(
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
    val postId: Int,
): Serializable
