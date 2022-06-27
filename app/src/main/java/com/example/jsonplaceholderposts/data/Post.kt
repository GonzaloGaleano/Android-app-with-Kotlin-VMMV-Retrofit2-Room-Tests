package com.example.jsonplaceholderposts.data

import java.io.Serializable

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    var favorite: Boolean,
    var user: User?,
    var comments: List<Comment>?
): Serializable