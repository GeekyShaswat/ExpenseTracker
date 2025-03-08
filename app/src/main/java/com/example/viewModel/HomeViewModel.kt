package com.example.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.database.ExpenseDao
import com.example.database.ExpenseDatabase
import com.example.database.ExpenseTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(dao : ExpenseDao) : ViewModel() {
    val dao = dao
    val expenseDao = dao.getAllExpenses()

    fun getBalance(list : List<ExpenseTable>) : Double {
        var balance = 0.0
        list.forEach {
            if(it.type == "Expense")
                balance -= it.amount.toDouble()
            else
            balance += it.amount.toInt()
        }
        return balance
    }

    fun getIncome(list : List<ExpenseTable>) : Double {
        var income = 0.0
        list.forEach {
            if(it.type == "Income")
                income += it.amount.toDouble()
        }
        return income
    }

    fun getExpense(list : List<ExpenseTable>) : Double {
        var expense = 0.0
        list.forEach {
            if(it.type == "Expense")
                expense += it.amount.toDouble()
        }
        return expense
    }

    fun deleteExpense(expense: ExpenseTable) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteExpense(expense)
        }
    }
}

class HomeViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}