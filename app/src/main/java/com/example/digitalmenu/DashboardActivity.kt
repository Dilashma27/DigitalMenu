package com.example.digitalmenu

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.digitalmenu.model.CartItemModel
import com.example.digitalmenu.model.ProductModel
import com.example.digitalmenu.ui.home.HomeScreen
import com.example.digitalmenu.ui.order.OrderScreen
import com.example.digitalmenu.ui.favorites.FavoritesScreen
import com.example.digitalmenu.ui.profile.ProfileScreen
import android.content.Intent
import com.example.digitalmenu.ViewModel.ProductViewModel
import com.example.digitalmenu.repository.ProductRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.example.digitalmenu.view.LoginActivity


class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}
data class NavItem(val label: String, val icon: Int)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody(){
    val context = LocalContext.current
    val activity = if (context is Activity) context else null

    val email = activity?.intent?.getStringExtra("email")?: ""
    val password = activity?.intent?.getStringExtra("password")?: ""

    var selectedIndex by remember { mutableStateOf(0) }
    val favoriteItems = remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    val cartItems = remember { mutableStateOf<List<CartItemModel>>(emptyList()) }
    
    // Scoped ViewModel to prevent recreation and allow real-time sync
    val productViewModel = remember { ProductViewModel(ProductRepoImpl()) }


    var listNav = listOf(
        NavItem(
            "Home",
            icon = R.drawable.baseline_home_24,
        ),
        NavItem(
            "Order",
            icon = R.drawable.baseline_restaurant_menu_24,
        ),
        NavItem(
            "Favorites",
            icon = R.drawable.baseline_favorite_border_24,
        ),

        NavItem(
            "Profile",
            icon = R.drawable.baseline_person_24,
        ),
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    actionIconContentColor = White,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.Black
                ),
                title= {
                    Text("Dashboard") },
                navigationIcon = {
                    IconButton (onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                            contentDescription = null
                        )
                    }
                },
                actions ={
                    IconButton(onClick = {

                    }){
                        Icon(
                            painter = painterResource(R.drawable.baseline_settings_24),
                            contentDescription = null
                        )
                    }
                    IconButton( onClick = {

                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_more_horiz_24),
                            contentDescription = null
                        )

                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedIndex == 0) {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, AddProductActivity::class.java)
                        context.startActivity(intent)
                    },
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                listNav.forEachIndexed { index,item->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val onAddToCart: (ProductModel) -> Unit = { item ->
                val existingItem = cartItems.value.find { it.product.productId == item.productId }
                if (existingItem != null) {
                    cartItems.value = cartItems.value.map {
                        if (it.product.productId == item.productId) it.copy(quantity = it.quantity + 1) else it
                    }
                } else {
                    cartItems.value = cartItems.value + CartItemModel(product = item, quantity = 1)
                }
                Toast.makeText(context, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
            }

            val onFavoriteToggle: (ProductModel) -> Unit = { item ->
                val isFavorite = favoriteItems.value.any { it.productId == item.productId }
                if (isFavorite) {
                    favoriteItems.value = favoriteItems.value.filter { it.productId != item.productId }
                    Toast.makeText(context, "Item removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteItems.value = favoriteItems.value + item
                    Toast.makeText(context, "Item added to favorites", Toast.LENGTH_SHORT).show()
                }
            }

            when(selectedIndex) {
                0 -> HomeScreen(
                    viewModel = productViewModel,
                    favoriteItems = favoriteItems.value,
                    onFavoriteToggle = onFavoriteToggle,
                    onAddToCart = onAddToCart,
                    onEditClick = { product ->
                        val intent = Intent(context, AddProductActivity::class.java).apply {
                            putExtra("productId", product.productId)
                            putExtra("name", product.name)
                            putExtra("description", product.description)
                            putExtra("price", product.price)
                            putExtra("categoryId", product.categoryId)
                            putExtra("image", product.image)
                        }
                        context.startActivity(intent)
                    }
                )
                1 -> OrderScreen(
                    cartItems = cartItems.value,
                    onIncrease = { item ->
                        cartItems.value = cartItems.value.map {
                            if (it.product.productId == item.product.productId) it.copy(quantity = it.quantity + 1) else it
                        }
                    },
                    onDecrease = { item ->
                        cartItems.value = cartItems.value.map {
                            if (it.product.productId == item.product.productId && it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
                        }
                    },
                    onRemove = { item ->
                        cartItems.value = cartItems.value.filter { it.product.productId != item.product.productId }
                    }
                )
                2 -> FavoritesScreen(
                    favoriteItems = favoriteItems.value,
                    onRemoveFavorite = onFavoriteToggle
                )
                3 -> ProfileScreen(
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }
                )
                else -> HomeScreen(
                    viewModel = productViewModel,
                    favoriteItems = favoriteItems.value,
                    onFavoriteToggle = onFavoriteToggle,
                    onAddToCart = onAddToCart,
                    onEditClick = { product ->
                        val intent = Intent(context, AddProductActivity::class.java).apply {
                            putExtra("productId", product.productId)
                            putExtra("name", product.name)
                            putExtra("description", product.description)
                            putExtra("price", product.price)
                            putExtra("categoryId", product.categoryId)
                            putExtra("image", product.image)
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}
@Preview
@Composable
fun DashboardActivityPreview(){
    DashboardBody()
}


