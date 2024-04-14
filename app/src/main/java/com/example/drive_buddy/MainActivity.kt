package com.example.drive_buddy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClickedd(view: View) {
        // Yol kayıt tuşuna basılınca CameraActivity'e yönlendirme
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
    fun onButtonClickedDistance(view: View) {
        // Yol kayıt tuşuna basılınca RoadDetectionActivity'e yönlendirme
        val intent = Intent(this, RoadDetectionActivity::class.java)
        startActivity(intent)
    }
}
