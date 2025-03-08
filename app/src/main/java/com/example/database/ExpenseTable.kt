package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double,
    val date: Long,
    val category: String,
    val type : String
)
