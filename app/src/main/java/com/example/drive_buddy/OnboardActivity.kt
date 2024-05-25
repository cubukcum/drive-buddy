package com.example.drive_buddy


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OnboardActivity : AppCompatActivity() {

    private fun onOnboardingFinished() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completedd", false)
        // Ana ekrana geç
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private lateinit var phoneFieldsContainer: LinearLayout
    private var phoneFieldCount = 0
    private val maxPhoneFields = 5
    private lateinit var firestore: FirebaseFirestore
    private val userId = FirebaseAuth.getInstance().currentUser // Firebase Authentication ile giriş yapan kullanıcının ID'si

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()


        phoneFieldsContainer = findViewById(R.id.phoneFieldsContainer)
        val addPhoneFieldButton: ImageView = findViewById(R.id.addPhoneFieldButton)
        val nextButton: TextView = findViewById(R.id.textViewFinish)

        firestore = FirebaseFirestore.getInstance()

        // Başlangıçta 3 telefon alanı ekle
        for (i in 0 until 1) {
            addPhoneField()
        }

        addPhoneFieldButton.setOnClickListener {
            if (phoneFieldCount < maxPhoneFields) {
                addPhoneField()
            }
        }

        nextButton.setOnClickListener {
            savePhoneNumbersToFirebase()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun addPhoneField() {
        val inflater = LayoutInflater.from(this)
        val phoneFieldView = inflater.inflate(R.layout.phone_field, phoneFieldsContainer, false)

        val removeButton: ImageView = phoneFieldView.findViewById(R.id.removeButton)
        removeButton.setOnClickListener {
            phoneFieldsContainer.removeView(phoneFieldView)
            phoneFieldCount--
        }

        phoneFieldsContainer.addView(phoneFieldView)
        phoneFieldCount++
    }

    private fun savePhoneNumbersToFirebase() {
        val phoneNumbers = mutableListOf<String>()

        for (i in 0 until phoneFieldsContainer.childCount) {
            val phoneFieldView = phoneFieldsContainer.getChildAt(i)
            val phoneEditText = phoneFieldView.findViewById<TextInputEditText>(R.id.phoneNumberEditText)
            val phoneNumber = phoneEditText.text.toString()

            if (phoneNumber.isNotEmpty()) {
                phoneNumbers.add(phoneNumber)
            }
        }

        if (phoneNumbers.isNotEmpty()) {
            val userSafePhones = hashMapOf(
                "safePhones" to phoneNumbers
            )

            firestore.collection("users").document(userId.toString())
                .set(userSafePhones)
                .addOnSuccessListener {
                    // Başarılı bir şekilde kaydedildi
                }
                .addOnFailureListener { e ->
                    // Hata oluştu
                    e.printStackTrace()
                }
        }
    }
}
