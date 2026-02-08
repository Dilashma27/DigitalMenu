package com.example.digitalmenu

import com.example.digitalmenu.model.ProductModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductModelTest {

    @Test
    fun productPrice_isCorrect() {
        val product = ProductModel(
            productId = "1",
            name = "Tea",
            description = "Hot & Sweet",
            price = 30.0,
            categoryId = "C1"
        )

        assertEquals(30.0, product.price, 0.0)
    }

    @Test
    fun productName_isCorrect() {
        val product = ProductModel(
            productId = "2",
            name = "Burger",
            description = "Fresh and Tasty",
            price = 150.0,
            categoryId = "C2"
        )

        assertEquals("Burger", product.name)
    }

    @Test
    fun productPrice_isCorrectAlternative() {
        val product = ProductModel(
            productId = "3",
            name = "Pizza",
            description = "Cheesy pizza",
            price = 300.0,
            categoryId = "C3"
        )

        assertEquals(500.0, product.price, 0.0)
    }
}
