package com.example.drive_buddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ActivitySignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    fun onCreateSignInClicked(view: View) {
        // Yol kayıt tuşuna basılınca CameraActivity'e yönlendirme
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
    }
}