package com.example.nsda_job_3_locationshareing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nsda_job_3_locationshareing.ViewModel.MyProfileViewModel
import com.example.nsda_job_3_locationshareing.databinding.ActivityMyProfileBinding
import com.example.nsda_job_3_locationshareing.repository.UserRepository
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private val viewModel by viewModels<MyProfileViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyProfileViewModel(UserRepository()) as T
            }
        }
    }

    private var selectedUserId = ""
    private var selectedEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedUserId = intent.getStringExtra("uid")!!
        selectedEmail = intent.getStringExtra("email")!!

        binding.email.text = selectedEmail
        if (UserRepository().getCurrentUserId() != selectedUserId) binding.btnShare.visibility = View.GONE

        FirebaseFirestore.getInstance().collection("users").document(selectedUserId)
            .get().addOnSuccessListener { doc ->
                binding.edtUsername.setText(doc.getString("username") ?: "")
            }

        binding.btnUpdateUsername.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            if (username.isNotEmpty()) viewModel.updateUsername(selectedUserId, username)

            val intent = Intent(this, FriendListActivity::class.java)
            startActivity(intent)
        }

        viewModel.usernameUpdateResult.observe(this) { success ->
            if (success) Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }

        binding.btnMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java).apply { putExtra("uid", selectedUserId) })
        }

        binding.btnShare.setOnClickListener { shareMyLocation() }
    }

    private fun shareMyLocation() {
        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        fusedLocation.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) viewModel.shareLocation(selectedUserId, loc.latitude, loc.longitude)
            Toast.makeText(this, "Location Updated!", Toast.LENGTH_SHORT).show()
        }
    }
}