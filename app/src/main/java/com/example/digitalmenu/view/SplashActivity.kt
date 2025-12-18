package com.example.digitalmenu.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.digitalmenu.R
import kotlinx.coroutines.delay
val gradientColors = listOf(
    Color(0xFFD1B3FF),
    Color(0xFF9BB7FF)
)

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashBody()

        }
    }
}
@Composable
fun SplashBody(){
    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect (Unit ) {
        delay(5000)
        val intent = Intent (context,
            LoginActivity:: class.java)
        context.startActivity(intent)
        activity.finish()


    }

    Scaffold { padding->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues = padding)
                .background(
                    brush = Brush.verticalGradient(colors = gradientColors)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Image(
                painter = painterResource(R.drawable.menulogo),
                contentDescription = "Digital Menu",
                modifier = Modifier.size(180.dp)
                    .clip(CircleShape)

            )
            Spacer(modifier = Modifier.height(100.dp))
            CircularProgressIndicator(
                color = Color.Blue
            )
        }

    }

}
@Preview
@Composable
fun SplashActivityPreview(){
    SplashBody()
}

