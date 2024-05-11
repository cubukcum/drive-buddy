package com.example.drive_buddy.view
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.drive_buddy.R
import com.example.drive_buddy.model.RentalCarModel
import com.example.drive_buddy.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.time.delay



@Composable
fun RentalCarMainScreen(navController: NavController,context: Context) {
    rememberSystemUiController().apply {
        setSystemBarsColor(RentalPrimary, darkIcons = false)
        setNavigationBarColor(RentalPrimary, false)
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(RentalPrimary)
    ) {

        Text(
            text = "Drive\nBuddy",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 64.dp, end = 24.dp),
            style = TextStyle(
                lineHeight = 54.sp,
                fontFamily = MuliBold,
                fontSize = 54.sp,
                color = White
            )
        )

        Text(
            text = "Güvenli Yolculuk",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 16.dp, end = 24.dp),
            style = TextStyle(fontFamily = MuliSemiBold, fontSize = 20.sp, color = White)
        )

        Spacer(modifier = Modifier.padding(top = 36.dp))

        var carState by remember { mutableStateOf(MoveAnimationState.START) }
        val offsetAnimation: Dp by animateDpAsState(
            if (carState == MoveAnimationState.START) 5.dp else 300.dp,

            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ) {
            carState = MoveAnimationState.START
        }
        Image(
            painter = painterResource(id = R.drawable.ic_rental_main),
            contentScale = ContentScale.Fit,
            alignment = Alignment.CenterEnd,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .absoluteOffset(x = offsetAnimation)
                .fillMaxHeight(0.7f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 54.dp)
                .fillMaxHeight(), verticalArrangement = Arrangement.Bottom
        ) {
            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({

            },2000)


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                onClick = {
                    carState = MoveAnimationState.END
                    navController.navigate("ikinci_sayfa")
                    playSound(context, R.raw.baslangic_sesi)

                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = White,
                    contentColor = RentalSecondary
                )
            ) {

                Text(
                    text = "Hadi Başlayalım",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        fontFamily = MuliSemiBold,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}

enum class MoveAnimationState {
    START, END
}