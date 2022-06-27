package com.example.jsonplaceholderposts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jsonplaceholderposts.data.Comment
import com.example.jsonplaceholderposts.data.Favorite
import com.example.jsonplaceholderposts.data.Post
import com.example.jsonplaceholderposts.data.User

@Database(entities = [Post::class, Comment::class, User::class, Favorite::class], version = 1)
abstract class PostsDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
}