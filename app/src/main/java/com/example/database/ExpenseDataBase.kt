package com.example.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ExpenseTable::class,UserTable::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null
        private val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                INSTANCE?.let { database ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        ExpenseDatabase(database.expenseDao())
//                    }
//                }
            }
        }

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_db"
                ).addCallback(callback)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
