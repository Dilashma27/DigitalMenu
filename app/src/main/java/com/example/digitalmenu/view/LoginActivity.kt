package com.example.digitalmenu.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.example.digitalmenu.DashboardActivity
import com.example.digitalmenu.R
import com.example.digitalmenu.ViewModel.UserViewModel
import com.example.digitalmenu.repository.UserRepoImpl
import com.example.digitalmenu.ui.theme.PurpleGrey40
import com.example.digitalmenu.ui.theme.PurpleGrey80
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val Blue = Color(0xFF2196F3)

    val userViewModel = remember { UserViewModel(repo = UserRepoImpl()) }
    val context = LocalContext.current
    val activity = context as? Activity


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val localEmail: String? = sharedPreferences.getString("email", "")
    val localPassword: String? = sharedPreferences.getString("password", "")
    val editor = sharedPreferences.edit()

    val gradientColors = listOf(
        Color(0xFFD1B3FF),
        Color(0xFF9BB7FF)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        val scrollState = rememberScrollState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.menulogo),

                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
            item {

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Digital Menu",
                    fontSize = 36.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Welcome, Glad to see you", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(40.dp))

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                            }) {
                                Text("Ok")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDialog = false
                            }) {
                                Text("Cancel")
                            }
                        },
                        title = {
                            Text("Confirm")
                        },
                        text = {
                            Text("Are you sure you want to delete")
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        )
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { data ->
                        email = data
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = {
                        Text("Email Address")
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Blue,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { data ->
                        password = data
                    },
                    placeholder = {
                        Text("Password")
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(R.drawable.baseline_visibility_24)
                                else
                                    painterResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Blue,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text("Remember Me", fontSize = 12.sp)
                    }

                    Text(
                        "Forgot Password?",
                        modifier = Modifier.clickable {
                            val intent = Intent(context, ForgotPassword::class.java)
                            context.startActivity(intent)
                            activity?.finish()
                        },
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                     userViewModel.login(email, password){
                         success,message->
                         if(success){
                             val intent = Intent(context, DashboardActivity::class.java)
                             context.startActivity(intent)
                         }else{
                            Toast.makeText(context,
                                message ,
                                Toast.LENGTH_LONG
                            ).show()
                         }
                     }
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .height(50.dp)
                ) {
                    Text("Login", fontSize = 16.sp)
                }

                // OR Divider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp, horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        "Or Login with",
                        modifier = Modifier.padding(horizontal = 15.dp),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    SocialMediaCard(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        image = R.drawable.gmail,
                        label = "Google"
                    )

                    SocialMediaCard(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        image = R.drawable.face,
                        label = "Facebook"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    buildAnnotatedString {
                        append("Don't have an account? ")
                        withStyle(SpanStyle(color = Blue, fontWeight = FontWeight.Bold)) {
                            append("Sign Up Now")
                        }
                    },
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(context, RegistrationActivity::class.java)
                            context.startActivity(intent)

                        }
                        .padding(vertical = 20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Composable
fun SocialMediaCard(modifier: Modifier, image: Int, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
fun LoginActivityPreview() {
    LoginBody()
}