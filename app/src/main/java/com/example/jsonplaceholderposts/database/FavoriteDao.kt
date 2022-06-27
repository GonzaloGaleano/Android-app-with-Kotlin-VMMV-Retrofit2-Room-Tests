package com.example.jsonplaceholderposts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jsonplaceholderposts.data.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(postId: Favorite)

    @Delete
    fun deleteFavorite(postId: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavorites(): List<Favorite>

    @Query("SELECT * FROM favorite")
    fun favoritesLive(): LiveData<List<Favorite>>

//    @Query("SELECT * FROM favorite WHERE post_id = :postId")
//    fun findFavorite(postId: Int): Favorite
}