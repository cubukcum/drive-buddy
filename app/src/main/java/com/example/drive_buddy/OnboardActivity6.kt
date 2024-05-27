package com.example.drive_buddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.drive_buddy.databinding.ActivityOnboard1Binding
import com.example.drive_buddy.databinding.ActivityOnboard2Binding
import com.example.drive_buddy.databinding.ActivityOnboard6Binding
import com.example.drive_buddy.databinding.ActivitySignInBinding

class OnboardActivity6 : AppCompatActivity() {
    private lateinit var binding: ActivityOnboard6Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboard6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewFinish.setOnClickListener{
            val intent = Intent(this, OnboardActivity7::class.java)
            startActivity(intent)
        }
    }
}