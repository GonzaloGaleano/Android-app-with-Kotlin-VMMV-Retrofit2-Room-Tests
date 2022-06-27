package com.example.jsonplaceholderposts.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    val postId: Int
): Serializable
