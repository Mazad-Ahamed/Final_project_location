package com.example.nsda_job_3_locationshareing.model

data class AppUsers(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)