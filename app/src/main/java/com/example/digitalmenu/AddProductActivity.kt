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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
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
                onSuccess = { finish() }
            )
        }
    }
}

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = ProductViewModel(ProductRepoImpl()),
    selectedImageUri: Uri?,
    initialProduct: ProductModel? = null,
    onPickImage: () -> Unit,
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var price by remember { mutableStateOf(initialProduct?.price?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(initialProduct?.categoryId ?: "Snacks") }

    val categories = listOf("Snacks", "Drinks", "Dessert")
    val gradientColors = listOf(Color(0xFFD1B3FF), Color(0xFF9BB7FF))

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(padding)
                .padding(16.dp)
        ) {
            // Image Picker Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onPickImage() }
                    .padding(10.dp)
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
                    Image(
                        painter = painterResource(R.drawable.placeholder),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Product Price") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Select Category", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFF6C63FF) else Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || price.isBlank()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    
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
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(50.dp)
            ) {
                Text(if (initialProduct == null) "Add Product" else "Update Product")
            }
        }
    }
}
