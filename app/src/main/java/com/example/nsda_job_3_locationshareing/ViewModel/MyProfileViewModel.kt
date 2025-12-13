package com.example.nsda_job_3_locationshareing.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nsda_job_3_locationshareing.repository.UserRepository

class MyProfileViewModel(private val repo: UserRepository) : ViewModel() {

    val usernameUpdateResult = MutableLiveData<Boolean>()

    fun updateUsername(userId: String, username: String) {
        repo.updateUsername(userId, username) { success ->
            usernameUpdateResult.value = success
        }
    }

    fun shareLocation(userId: String, lat: Double, lng: Double) {
        repo.updateLocation(userId, lat, lng)
    }
}