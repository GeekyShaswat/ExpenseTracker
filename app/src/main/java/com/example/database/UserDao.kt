package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {
    @Query("SELECT * FROM UserTable WHERE name = :name")
    suspend fun usernameExist(name: String): UserTable?

    @Insert
    suspend fun insertUser(user: UserTable)

    @Query("SELECT * FROM UserTable WHERE name = :name AND password = :password")
    suspend fun checkUser(name: String , password : String) : UserTable?

}