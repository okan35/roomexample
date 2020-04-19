package com.example.roomexample.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomexample.data.AppDatabase
import com.example.roomexample.data.User
import com.example.roomexample.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: LiveData<List<User>>

    init {
        val usersDao = AppDatabase.getDatabase(
            application,
            viewModelScope
        ).userDao()
        repository = UserRepository(usersDao)
        allUsers = repository.allUsers
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}