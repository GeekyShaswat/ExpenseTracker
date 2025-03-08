package com.example.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.database.userdatabase.ExpenseSummary
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Upsert()
    suspend fun insertExpense(expense: ExpenseTable)

    @Delete()
    suspend fun deleteExpense(expense: ExpenseTable)

    @Query("SELECT * FROM ExpenseTable")
    fun getAllExpenses(): Flow<List<ExpenseTable>>

    @Query("SELECT type , date , SUM(amount) as total FROM ExpenseTable where type =  :type GROUP BY type, date ORDER BY date ")
    fun getAllExpenseByDate(type : String ="Expense"):Flow<List<ExpenseSummary>>

}