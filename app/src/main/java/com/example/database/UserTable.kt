package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String
)
