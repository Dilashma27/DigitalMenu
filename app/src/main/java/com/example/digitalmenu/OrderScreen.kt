package com.example.digitalmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun OrderScreen(){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Text("OrderScreen", style= TextStyle(Color.Black))
    }
}

