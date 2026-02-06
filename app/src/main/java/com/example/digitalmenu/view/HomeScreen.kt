package com.example.digitalmenu.ui.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitalmenu.R
import com.example.digitalmenu.model.ProductModel

val gradientColors = listOf(
    Color(0xFFD1B3FF),
    Color(0xFF9BB7FF)
)

@Composable
fun HomeScreen(
    favoriteItems: List<ProductModel> = emptyList(),
    onFavoriteToggle: (ProductModel) -> Unit = {}
) {
    // Define menu items using ProductModel
    val menuItems = listOf(
        ProductModel(
            productId = "1",
            name = "Margherita Pizza",
            description = "Classic Italian pizza",
            price = 299.0,
            categoryId = "snacks",
            image = ""
        ),
        ProductModel(
            productId = "2",
            name = "Burger",
            description = "Delicious burger",
            price = 250.0,
            categoryId = "snacks",
            image = ""
        ),
        ProductModel(
            productId = "3",
            name = "Mo:Mo",
            description = "Steamed dumplings",
            price = 150.0,
            categoryId = "snacks",
            image = ""
        ),
        ProductModel(
            productId = "4",
            name = "Chocolava",
            description = "Chocolate lava cake",
            price = 100.0,
            categoryId = "dessert",
            image = ""
        )
    )

    // Search state
    var searchText by remember { mutableStateOf("") }

    // Selected category state
    var selectedCategory by remember { mutableStateOf("All") }

    // Filtered items based on search and category
    val filteredItems = remember(searchText, selectedCategory, menuItems) {
        var items = menuItems

        // Filter by category
        if (selectedCategory != "All") {
            items = items.filter { it.categoryId.equals(selectedCategory, ignoreCase = true) }
        }

        // Filter by search text
        if (searchText.isNotBlank()) {
            items = items.filter { item ->
                item.name.contains(searchText, ignoreCase = true) ||
                        item.description.contains(searchText, ignoreCase = true)
            }
        }

        items
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader()
            }

            item {
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )
            }

            item {
                CategorySection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // Show message if no results found
            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchText.isNotBlank()) {
                                "No items found for \"$searchText\""
                            } else {
                                "No items in this category"
                            },
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(filteredItems) { item ->
                    val drawableRes = when (item.productId) {
                        "1" -> R.drawable.pizza
                        "2" -> R.drawable.burger
                        "3" -> R.drawable.momo
                        "4" -> R.drawable.chocolava
                        else -> R.drawable.pizza
                    }

                    MenuItemCard(
                        product = item,
                        imageRes = drawableRes,
                        isFavorite = favoriteItems.any { it.productId == item.productId },
                        onFavoriteClick = { onFavoriteToggle(item) }
                    )
                }
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
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
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
fun CategorySection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("All", "Snacks", "Drinks", "Dessert")

    LazyRow(
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories.size) { index ->
            CategoryChip(
                text = categories[index],
                isSelected = selectedCategory == categories[index],
                onClick = { onCategorySelected(categories[index]) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF6C63FF) else Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun MenuItemCard(
    product: ProductModel,
    imageRes: Int,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rs. ${product.price.toInt()}", color = Color.Black)
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}