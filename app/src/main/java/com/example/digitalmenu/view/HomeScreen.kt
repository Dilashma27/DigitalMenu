package com.example.digitalmenu.ViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


val gradientColors = listOf(
    Color(0xFFD1B3FF),
    Color(0xFF9BB7FF)
)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(gradientColors)
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                HomeHeader()
            }

            item {
                SearchBar()
            }

            item {
                CategorySection()
            }

            items(5) {
                MenuItemCard(
                    name = "Margherita Pizza",
                    price = "Rs. 299"
                )
            }
        }

    }
}


@Composable
fun HomeHeader() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Digital Menu 🍽️",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Choose your favorite food",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Search food...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6C63FF),
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@Composable
fun CategorySection() {
    val categories = listOf("Pizza", "Burger", "Drinks", "Dessert", "Coffee")

    LazyRow(
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
            items(categories.size) { index ->
                CategoryChip(text = categories[index])
            }
    }
}

@Composable
fun CategoryChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.clickable { }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MenuItemCard(
    name: String,
    price: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Fake image placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFEAEAEA), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(price, color = Color.Gray)
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

