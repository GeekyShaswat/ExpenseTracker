package com.example.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.database.ExpenseDatabase
import com.example.database.UserDao
import com.example.database.UserTable
import java.lang.Exception
import androidx.compose.runtime.*


class UserViewModel(val dao : UserDao) : ViewModel() {

    suspend fun checkUser(username : String , password : String) : Boolean{
        val user = dao.checkUser(username,password)
        return user == null
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        return dao.usernameExist(username) == null
    }


    suspend fun insertUser(username : String , password : String) : Boolean{
        val user = UserTable(name = username, password = password)
        return try {
            dao.insertUser(user)
            true
        }
        catch (e : Exception){
            false
        }
    }
}


class UserViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).userDao()
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}