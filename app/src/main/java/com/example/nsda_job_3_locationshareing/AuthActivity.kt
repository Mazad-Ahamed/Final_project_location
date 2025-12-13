package com.example.nsda_job_3_locationshareing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nsda_job_3_locationshareing.ViewModel.AuthViewModel
import com.example.nsda_job_3_locationshareing.databinding.ActivityAuthBinding
import com.example.nsda_job_3_locationshareing.repository.UserRepository

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(UserRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            viewModel.login(email, pass, this)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            viewModel.register(email, pass, this)
        }

        viewModel.loginResult.observe(this) { (success, msg) ->
            if (success) {
                // ✅ Send firstLogin flag
                val intent = Intent(this, FriendListActivity::class.java)
                intent.putExtra("firstLogin", true) // important!
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else Toast.makeText(this, msg ?: "Login failed", Toast.LENGTH_SHORT).show()
        }

        viewModel.registerResult.observe(this) { (success, msg) ->
            if (success) {
                // ✅ Send firstLogin flag
                val intent = Intent(this, FriendListActivity::class.java)
                intent.putExtra("firstLogin", true) // important!
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else Toast.makeText(this, msg ?: "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }
}