package com.example.digitalmenu

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage

import com.example.digitalmenu.ViewModel.ProductViewModel
import com.example.digitalmenu.model.ProductModel
import com.example.digitalmenu.repository.ProductRepoImpl
import com.example.digitalmenu.ui.theme.ImageUtils



import java.util.UUID

class AddProductActivity : ComponentActivity() {
    lateinit var imageUtils: ImageUtils
    var selectedImageUri by mutableStateOf<Uri?>(null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        imageUtils = ImageUtils(this, this)
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



    Scaffold(
        topBar = {
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues = padding)
                .padding(16.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onPickImage()
                    }
                    .padding(10.dp)
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (initialProduct?.image != null) {
                    AsyncImage(
                        model = initialProduct.image,
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(R.drawable.placeholder),
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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (name.isBlank() || description.isBlank() || price.isBlank()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val saveProduct: (String?) -> Unit = { imageUrl ->
                        val product = ProductModel(
                            productId = initialProduct?.productId ?: UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            price = price.toDouble(),
                            categoryId = initialProduct?.categoryId ?: "",
                            image = imageUrl ?: initialProduct?.image
                        )

                        if (initialProduct == null) {
                            viewModel.addProduct(product) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                } else {
                                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            viewModel.updateProduct(product) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                } else {
                                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    if (selectedImageUri != null) {
                        viewModel.uploadImage(context, selectedImageUri) { imageUrl ->
                            saveProduct(imageUrl)
                        }
                    } else {
                        saveProduct(null)
                    }

                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (initialProduct == null) "Add Product" else "Update Product")
            }

            Spacer(modifier = Modifier.height(20.dp))


        }

    }
}



@Preview
@Composable
fun ProductActivityPreview() {
    ProductScreen(
        selectedImageUri = null, // or pass a mock Uri if needed
        onPickImage = {} // no-op
    )
}

