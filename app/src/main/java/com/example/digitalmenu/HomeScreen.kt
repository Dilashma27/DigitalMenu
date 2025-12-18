package com.example.digitalmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.digitalmenu.ui.theme.DigitalMenuTheme
@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        Text("HomeScreen", style = TextStyle(Color.Black))
    }
}