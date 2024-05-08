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
    fun onButtonClickedDistance1(view: View) {
        // Yol kayıt tuşuna basılınca RoadDetectionActivity'e yönlendirme
        val intent = Intent(this, LaneDetectionActivity::class.java)
        startActivity(intent)
    }
    fun onButtonClickedDistance2(view: View) {
        // Yol kayıt tuşuna basılınca RoadDetectionActivity'e yönlendirme
        val intent = Intent(this, WeaponDetectionActivity::class.java)
        startActivity(intent)
    }
    fun onButtonClickedDistance3(view: View) {
        // Yol kayıt tuşuna basılınca RoadDetectionActivity'e yönlendirme
        val intent = Intent(this, SignDetectionActivity::class.java)
        startActivity(intent)
    }
    fun onButtonClickedDistance4(view: View) {
        // Yol kayıt tuşuna basılınca RoadDetectionActivity'e yönlendirme
        val intent = Intent(this, DrowsinessClassificationActivity::class.java)
        startActivity(intent)
    }
}
