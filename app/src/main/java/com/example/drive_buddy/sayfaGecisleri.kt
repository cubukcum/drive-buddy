package com.example.drive_buddy

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drive_buddy.model.RentalCarModel
import com.example.drive_buddy.view.RentalCarDetailScreen
import com.example.drive_buddy.view.RentalCarMainScreen
import androidx.fragment.app.viewModels
import com.example.drive_buddy.model.CarSpec

@Composable
fun sayfaGecisleri(){

val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "ilk_sayfa"){

        composable(route="ilk_sayfa"){
            RentalCarMainScreen(navController, context = LocalContext.current)
        }

        composable("signin") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, SignInActivity::class.java)
            LocalContext.current.startActivity(intent)

        }

        composable("uyku_takip") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, DrowsinessClassificationActivity::class.java)
            LocalContext.current.startActivity(intent)

        }

        composable("serit_takip") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, LaneDetectionActivity::class.java)
            LocalContext.current.startActivity(intent)

        }

        composable("silah_takip") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, WeaponDetectionActivity::class.java)
            LocalContext.current.startActivity(intent)

        }
        composable("trafik_takip") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, SignDetectionActivity::class.java)
            LocalContext.current.startActivity(intent)

        }

        composable("arac_takip") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, RoadDetectionActivity::class.java)
            LocalContext.current.startActivity(intent)

        }

        composable("camera_screen") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, CameraActivity::class.java)
            LocalContext.current.startActivity(intent)
        }

        composable("harita_screen") {
            // Kamera ekranına geçiş yapmak için intent oluşturun ve başlatın
            val intent = Intent(LocalContext.current, MapActivity::class.java)
            LocalContext.current.startActivity(intent)
        }

        composable(route="ikinci_sayfa"){
            RentalCarDetailScreen(navController, car = RentalCarModel(model = "drive Buddy", brand = "Drive Buddy",
                specs = arrayListOf(
                    CarSpec(
                        "Uyku Takip",

                        R.drawable.sleep
                    ),
                    CarSpec(
                        "Şerit Takip",
                        R.drawable.road3
                    ),
                    CarSpec(
                        "Silah Takip",
                        R.drawable.keles
                    ),
                    CarSpec(
                        "Fren Uyarı",
                        R.drawable.map3
                    ),
                    CarSpec(
                        "Trafik İşareti",
                        R.drawable.traffic_light
                    ),
                    CarSpec(
                        "Yol Kayıt",
                        R.drawable.camera
                    )
                ),
                dealerImage = R.drawable.ic_rental_pp,
                image = R.drawable.ic_rental_detail))
        }
    }



}
@Composable
fun BackHandler(onBack: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(lifecycleOwner, backDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }

        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
}


