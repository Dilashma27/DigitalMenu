package com.example.digitalmenu.ui.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitalmenu.R
import com.example.digitalmenu.ViewModel.ProductViewModel
import com.example.digitalmenu.model.ProductModel
import com.example.digitalmenu.AddProductActivity

val gradientColors = listOf(
    Color(0xFFD1B3FF),
    Color(0xFF9BB7FF)
)

@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    favoriteItems: List<ProductModel> = emptyList(),
    onFavoriteToggle: (ProductModel) -> Unit = {},
    onAddToCart: (ProductModel) -> Unit,
    onEditClick: (ProductModel) -> Unit = {},
    onDeleteClick: (ProductModel) -> Unit = {}
) {
    val context = LocalContext.current
    // Fetch real products from ViewModel
    val menuItems by viewModel.allProducts.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        if (viewModel.allProducts.value.isNullOrEmpty()) {
            viewModel.getAllProduct()
        }
    }

    var showDeleteDialog by remember { mutableStateOf<ProductModel?>(null) }

    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Product") },
            text = { Text("Are you sure you want to delete ${showDeleteDialog?.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog?.let { product ->
                        // Optimistic UI update: remove from local state immediately
                        val currentList = viewModel.allProducts.value ?: emptyList()
                        viewModel.allProducts.postValue(currentList.filter { it.productId != product.productId })

                        viewModel.deleteProduct(product.productId) { success, message ->
                            if (!success) {
                                // Revert or show error if deletion failed
                                viewModel.getAllProduct()
                            }
                        }
                    }
                    showDeleteDialog = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }


    // Search state
    var searchText by remember { mutableStateOf("") }

    // Selected category state
    var selectedCategory by remember { mutableStateOf("All") }

    // Filtered items based on search and category
    val filteredItems = remember(searchText, selectedCategory, menuItems) {
        var items = menuItems ?: emptyList()

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (searchText.isNotBlank()) {
                            "No items found for \"$searchText\""
                        } else if (selectedCategory != "All") {
                            "No items in category \"$selectedCategory\""
                        } else {
                            "Your menu is empty!"
                        },
                        color = Color.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (searchText.isBlank() && selectedCategory == "All") {
                        Button(
                            onClick = {
                                val intent = Intent(context, AddProductActivity::class.java)
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6C63FF)
                            )
                        ) {
                            Text("Add Your First Product", color = Color.White)
                        }
                    }
                }
            }
        } else {
            items(filteredItems) { item ->
                MenuItemCard(
                    product = item,
                    isFavorite = favoriteItems.any { it.productId == item.productId },
                    onFavoriteClick = { onFavoriteToggle(item) },
                    onAddToCart = { onAddToCart(item) },
                    onEditClick = { onEditClick(item) },
                    onDeleteClick = { showDeleteDialog = item }
                )
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Digital Menu",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Choose your favorite food",
            color = Color.Black,
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
            unfocusedBorderColor = Color.White
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
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onAddToCart: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
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
            val imageUrl = product.image
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.placeholder), // Shows placeholder on error
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rs. ${product.price.toInt()}", color = Color.Black)

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF6C63FF),
                    modifier = Modifier.clickable { onAddToCart() }
                ) {
                    Text(
                        text = "Add to Cart",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}