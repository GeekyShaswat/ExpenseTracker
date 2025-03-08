package com.example.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.database.ExpenseDao
import com.example.database.ExpenseDatabase
import com.example.database.ExpenseTable
import java.lang.Exception

class AddExpenseViewModel(val dao : ExpenseDao) : ViewModel(){

    suspend fun addExpense(entity : ExpenseTable): Boolean{
        return try {
            dao.insertExpense(entity)
             true
        }
        catch (e : Exception){
            false
        }
    }
}

class AddExpenseViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddExpenseViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}
