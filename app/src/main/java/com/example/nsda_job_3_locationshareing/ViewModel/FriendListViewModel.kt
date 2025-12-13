package com.example.nsda_job_3_locationshareing.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nsda_job_3_locationshareing.model.AppUsers
import com.example.nsda_job_3_locationshareing.repository.UserRepository

class FriendListViewModel(private val repo: UserRepository) : ViewModel() {

    val usersList = MutableLiveData<List<AppUsers>>()

    fun fetchUsers() {
        repo.getAllUsers { list ->
            usersList.value = list
        }
    }

    fun logout() {
        repo.logout()
    }
}