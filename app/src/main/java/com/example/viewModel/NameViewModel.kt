package com.example.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NameViewModel : ViewModel() {
    private var _username = MutableStateFlow("")
    var username: StateFlow<String> = _username
    init {
        Log.d("NameViewModel", "NameViewModel initialized with ${username.value}")
    }

    fun setUsername(name: String) {
        _username.value = name.toString()
        Log.d("NameViewModel", "Username updated: $name")
    }

}
