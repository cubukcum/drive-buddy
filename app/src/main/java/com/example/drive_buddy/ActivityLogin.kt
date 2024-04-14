package com.example.drive_buddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginButtonClicked(view: View) {
        // Yol kayıt tuşuna basılınca CameraActivity'e yönlendirme
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onCreateAccountClicked(view: View) {
        // Yol kayıt tuşuna basılınca CameraActivity'e yönlendirme
        val intent = Intent(this, ActivitySignIn::class.java)
        startActivity(intent)
    }
}