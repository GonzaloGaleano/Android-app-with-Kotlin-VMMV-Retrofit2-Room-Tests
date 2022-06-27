package com.example.jsonplaceholderposts.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User (
    @PrimaryKey
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
        ): Serializable {
            fun getAtUsername(): String {
                return "@${this.username}"
            }
        }