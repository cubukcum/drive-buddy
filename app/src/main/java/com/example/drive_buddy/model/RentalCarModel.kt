package com.example.drive_buddy.model

import androidx.annotation.DrawableRes
import com.example.drive_buddy.R

data class RentalCarModel(
    val brand: String = "",
    val model: String = "",
    val price: Int = 0,
    val specs: ArrayList<CarSpec> = arrayListOf(),
    @DrawableRes val dealerImage: Int = 0,
    @DrawableRes val image: Int = 0
) {
    val mock: RentalCarModel
        get() {
            return RentalCarModel(
                brand = "FERRARI",
                model = "2022 Ferrari 812 GTS",
                price = 250,
                specs = arrayListOf(
                    CarSpec(
                        "TOP SPEED",
                        R.drawable.ic_rental_speedometer
                    ),
                    CarSpec(
                        "POWER",
                        R.drawable.ic_rental_horsepower
                    ),
                    CarSpec(
                        "SEATS",
                        R.drawable.ic_rental_seat
                    ),
                    CarSpec(
                        "DOORS",
                        R.drawable.ic_rental_car_door
                    ),
                    CarSpec(
                        "FUEL",
                        R.drawable.ic_rental_fuel
                    ),
                    CarSpec(
                        "SENSOR",
                        R.drawable.ic_rental_sensor
                    )
                ),
                dealerImage = R.drawable.ic_rental_pp,
                image = R.drawable.ic_rental_detail
            )
        }
}

data class CarSpec(
    val title: String,
    @DrawableRes val icon: Int
)