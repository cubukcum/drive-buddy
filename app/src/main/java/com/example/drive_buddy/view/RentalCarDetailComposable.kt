package com.example.drive_buddy.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drive_buddy.*
import com.example.drive_buddy.R
import com.example.drive_buddy.model.CarSpec
import com.example.drive_buddy.model.RentalCarModel
import com.example.drive_buddy.theme.LightGrey2
import com.example.drive_buddy.theme.MuliBold
import com.example.drive_buddy.theme.MuliRegular
import com.example.drive_buddy.theme.MuliSemiBold
import com.example.drive_buddy.theme.RentalPrimary
import com.example.drive_buddy.theme.RentalSecondary
import com.example.drive_buddy.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth



import android.media.MediaPlayer
import android.content.Context
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


fun playSound(context: Context, resId: Int) {
    val mediaPlayer = MediaPlayer.create(context, resId)
    mediaPlayer?.start()

    mediaPlayer?.setOnCompletionListener {
        mediaPlayer.release()
    }
}


@Composable
fun RentalCarDetailScreen(navController: NavController,car: RentalCarModel) {
    rememberSystemUiController().apply {
        setSystemBarsColor(RentalSecondary, darkIcons = false)
        setNavigationBarColor(LightGrey2, false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(LightGrey2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(RentalSecondary)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                //geri tuşu

                Button(
                    onClick = {
                              navController.navigate("ilk_sayfa")

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = RentalPrimary,
                        contentColor = RentalSecondary
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                        .background(RentalPrimary, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "",
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                }


                Text(
                    text = car.brand,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    style = TextStyle(fontSize = 20.sp, fontFamily = MuliRegular, color = Color.White)
                )

                // Menüyü göstermek için açılır menü durumu
                var isMenuVisible by remember { mutableStateOf(false) }

                // Menü öğeleri
                val menuItems = listOf(
                    "Profil",
                    "Ayarlar",
                    "Çıkış"
                )



                // Buton ve menüyü birlikte içeren bileşen
                Column {
                    Button(
                        onClick = { isMenuVisible = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = RentalSecondary,
                            contentColor = RentalSecondary
                        )
                    ) {
                        Image(
                            painter = painterResource(id = car.dealerImage),
                            contentDescription = "",
                            modifier = Modifier
                                .height(48.dp)
                                .width(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .align(Alignment.CenterVertically),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Açılır menüyü göstermek için bir alt bileşen
                    DropdownMenu(
                        expanded = isMenuVisible,
                        onDismissRequest = { isMenuVisible = false }
                    ) {
                        menuItems.forEach { item ->
                            DropdownMenuItem(onClick = {

                                if(item=="Çıkış"){
                                    FirebaseAuth.getInstance().signOut()
                                    navController.navigate("signin"){
                                        popUpTo("ikinci_sayfa"){
                                            inclusive=true
                                        }
                                    }
                                }


                                // Menü öğesine tıklandığında yapılacak işlemler
                                // Örneğin, seçilen öğe üzerinde işlem yapabilir veya
                                // Başka bir ekrana geçiş yapabilirsiniz.
                                // Bu örnekte, sadece menüyü gizliyoruz.
                                isMenuVisible = false
                            }) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            Text(
                text = car.model,
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp)
                    .fillMaxWidth(0.7f)
                    .wrapContentHeight(),
                style = TextStyle(
                    fontSize = 18.sp, fontFamily = MuliRegular, color = Color.White
                )
            )

            Image(
                painter = painterResource(id = car.image),
                contentDescription = "",
                modifier = Modifier
                    .padding(28.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            contentPadding = PaddingValues(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(car.specs) {
                CarSpecItem(it,navController, LocalContext.current)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Güvenli Sürüş",
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = MuliBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            Button(
                modifier = Modifier
                    .width(164.dp)
                    .height(56.dp),
                onClick = {
                          print("bastın")

                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = RentalPrimary,
                    contentColor = RentalSecondary
                )
            ) {

                Text(
                    text = "Drive Buddy",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        fontFamily = MuliSemiBold,
                        fontSize = 20.sp,
                        color = White
                    )
                )
            }
        }
    }
}

@Composable
fun CarSpecItem(spec: CarSpec,navController: NavController,context: Context) {

    Button(onClick = {
        when (spec.title) {
            "Yol Kayıt" -> {
                navController.navigate("camera_screen")
            }
            "Harita" -> {
                navController.navigate("harita_screen")

            }
            "Uyku Takip" -> {
                navController.navigate("uyku_takip")
            }

        }



    }, modifier = Modifier
        .height(128.dp)
        .width(128.dp)
        .padding(8.dp)
        .background(RentalSecondary, RoundedCornerShape(12.dp)), colors = ButtonDefaults.buttonColors(
        RentalSecondary)) {

        Column(
            modifier = Modifier
                .height(128.dp)
                .width(128.dp)
                .padding(8.dp)
                .background(RentalSecondary, RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = spec.icon),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )

            Text(
                text = spec.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = MuliRegular,
                    color = LightGrey2,
                    textAlign = TextAlign.Center
                )
            )
        }
    }


}
