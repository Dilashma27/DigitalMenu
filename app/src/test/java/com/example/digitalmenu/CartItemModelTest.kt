package com.example.digitalmenu

import com.example.digitalmenu.model.CartItemModel
import com.example.digitalmenu.model.ProductModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CartItemModelTest {

    @Test
    fun cartItemQuantity_isCorrect() {
        val product = ProductModel(
            productId = "1",
            name = "Tea",
            description = "Hot & Sweet",
            price = 30.0,
            categoryId = "C1"
        )
        val cartItem = CartItemModel(product, 2)

        assertEquals(2, cartItem.quantity)
    }

    @Test
    fun cartItemProduct_isCorrect() {
        val product = ProductModel(
            productId = "2",
            name = "Burger",
            description = "Fresh and Tasty",
            price = 150.0,
            categoryId = "C2"
        )
        val cartItem = CartItemModel(product, 1)

        assertEquals("Burger", cartItem.product.name)
        assertEquals(150.0, cartItem.product.price, 0.0)
    }
}
