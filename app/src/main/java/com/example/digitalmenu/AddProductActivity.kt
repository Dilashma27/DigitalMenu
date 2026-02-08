package com.example.digitalmenu

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.digitalmenu.ViewModel.ProductViewModel
import com.example.digitalmenu.model.ProductModel
import com.example.digitalmenu.repository.ProductRepoImpl
import com.example.digitalmenu.ui.theme.ImageUtils
import java.util.UUID

class AddProductActivity : ComponentActivity() {
    private lateinit var imageUtils: ImageUtils
    private var selectedImageUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        imageUtils = ImageUtils(this, this)
        imageUtils.registerLaunchers { uri ->
            selectedImageUri = uri
        }

        val productId = intent.getStringExtra("productId")
        val initialProduct = if (productId != null) {
            ProductModel(
                productId = productId,
                name = intent.getStringExtra("name") ?: "",
                description = intent.getStringExtra("description") ?: "",
                price = intent.getDoubleExtra("price", 0.0),
                categoryId = intent.getStringExtra("categoryId") ?: "",
                image = intent.getStringExtra("image")
            )
        } else null

        setContent {
            ProductScreen(
                selectedImageUri = selectedImageUri,
                initialProduct = initialProduct,
                onPickImage = { imageUtils.launchImagePicker() },
                onSuccess = { finish() },
                onBack = { finish() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = ProductViewModel(ProductRepoImpl()),
    selectedImageUri: Uri?,
    initialProduct: ProductModel? = null,
    onPickImage: () -> Unit,
    onSuccess: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var price by remember { mutableStateOf(initialProduct?.price?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(initialProduct?.categoryId ?: "Snacks") }

    val categories = listOf("Snacks", "Drinks", "Dessert")
    val gradientColors = listOf(Color(0xFFD1B3FF), Color(0xFF9BB7FF))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialProduct == null) "Add Product" else "Edit Product",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Image Picker Box - Professional Design
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onPickImage() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (!initialProduct?.image.isNullOrEmpty()) {
                        AsyncImage(
                            model = initialProduct?.image,
                            contentDescription = "Product Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.placeholder),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Fit,
                                alpha = 0.6f
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tap to add image",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Add icon overlay for empty state
                    if (selectedImageUri == null && initialProduct?.image.isNullOrEmpty()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            shape = RoundedCornerShape(50),
                            color = Color(0xFF6C63FF),
                            shadowElevation = 4.dp
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Image",
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Product Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Product Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product Price Field
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Product Price") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Category Section
            Text(
                text = "Select Category",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF2D2D2D)
            )
            Spacer(modifier = Modifier.height(12.dp))

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(25.dp),
                        color = if (isSelected) Color(0xFF6C63FF) else Color.White,
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            color = if (isSelected) Color.White else Color(0xFF666666),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button - Smaller and more professional
            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || price.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val priceValue = price.toDoubleOrNull()
                    if (priceValue == null) {
                        Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val saveProduct: (String?) -> Unit = { imageUrl ->
                        val product = ProductModel(
                            productId = initialProduct?.productId ?: UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            price = priceValue,
                            categoryId = selectedCategory,
                            image = imageUrl ?: initialProduct?.image
                        )

                        if (initialProduct == null) {
                            viewModel.addProduct(product) { success, _ ->
                                if (success) {
                                    Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                } else {
                                    Toast.makeText(context, "Error adding product", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            viewModel.updateProduct(product) { success, _ ->
                                if (success) {
                                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                } else {
                                    Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    if (selectedImageUri != null) {
                        viewModel.uploadImage(context, selectedImageUri) { imageUrl ->
                            if (imageUrl != null) {
                                saveProduct(imageUrl)
                            } else {
                                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        saveProduct(null)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(180.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = if (initialProduct == null) "Add Product" else "Update Product",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}