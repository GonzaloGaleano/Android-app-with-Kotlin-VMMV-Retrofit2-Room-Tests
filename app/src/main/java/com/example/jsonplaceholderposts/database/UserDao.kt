package com.example.jsonplaceholderposts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jsonplaceholderposts.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun loadUsers(): LiveData<List<User>>
}