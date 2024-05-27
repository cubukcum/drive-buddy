package com.example.drive_buddy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.drive_buddy.databinding.ActivityOnboard1Binding
import com.example.drive_buddy.databinding.ActivityOnboard2Binding
import com.example.drive_buddy.databinding.ActivitySignInBinding

class OnboardActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityOnboard2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val nextButton: TextView = findViewById(R.id.textViewFinish)

        nextButton.setOnClickListener{
            val intent = Intent(this, OnboardActivity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
            finish()
        }
    }
}