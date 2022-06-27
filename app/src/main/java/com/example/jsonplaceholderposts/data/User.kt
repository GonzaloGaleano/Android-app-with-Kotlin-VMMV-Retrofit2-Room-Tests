package com.example.jsonplaceholderposts.data

import java.io.Serializable

data class User (
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