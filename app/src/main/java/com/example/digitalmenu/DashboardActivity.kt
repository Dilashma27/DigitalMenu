package com.example.digitalmenu

import android.app.Activity
import android.os.Bundle
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
import com.example.digitalmenu.ui.home.HomeScreen
import com.example.digitalmenu.ui.order.OrderScreen
import com.example.digitalmenu.ui.favorites.FavoritesScreen
import com.example.digitalmenu.ui.profile.ProfileScreen


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
            when(selectedIndex){
                0-> HomeScreen()
                1-> OrderScreen()
                2-> FavoritesScreen()
                3-> ProfileScreen()
                else -> HomeScreen()
            }
        }
    }
}
@Preview
@Composable
fun DashboardActivityPreview(){
    DashboardBody()
}


