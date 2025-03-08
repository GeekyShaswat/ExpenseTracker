package com.example.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.database.ExpenseDao
import com.example.database.ExpenseDatabase
import com.example.database.ExpenseTable
import com.example.database.userdatabase.ExpenseSummary
import com.example.utils.Utils
import com.github.mikephil.charting.data.Entry

class StatsViewModel(dao : ExpenseDao) : ViewModel(){

    val entries = dao.getAllExpenseByDate()
    init {
        Log.d("StatsViewModel", "ViewModel Created!")

    }

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        Log.d("StatsViewModel", "Entries received: ${entries.size}") // Debug log
        for (entry in entries) {
            val formattedDate = entry.date.toLong() // Assuming `entry.date` is already in millis
            Log.d("ChartData", "Fixed Date: $formattedDate, Total: ${entry.total}")
            list.add(Entry(formattedDate.toFloat(), entry.total.toFloat()))
        }

        Log.d("StatsViewModel", "Final list: $list") // Check if multiple entries are added
        return list
    }


}
class StatsViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}