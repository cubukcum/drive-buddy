package com.example.drive_buddy

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.drive_buddy.model.RentalCarModel

class RentalCarViewModel(application: Application) : AndroidViewModel(application) {

    val rentalCarState: MutableState<RentalCarModel?> = mutableStateOf(null)

    init {
        rentalCarState.value = RentalCarModel().mock
    }
}