package com.example.digitalmenu

import com.example.digitalmenu.model.CartItemModel
import com.example.digitalmenu.model.ProductModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CartItemModelTest {

    @Test
    fun cartItemTotalPrice_isCorrect() {
        val product = ProductModel(
            productId = "4",
            name = "Coffee",
            description = "Hot coffee",
            price = 50.0,
            categoryId = "C4"
        )

        val cartItem = CartItemModel(product, 3)

        val totalPrice = cartItem.product.price * cartItem.quantity

        assertEquals(150.0, totalPrice, 0.0)
    }


    @Test
    fun cartItemQuantity_isWrong_expectedFail() {
        val product = ProductModel(
            productId = "5",
            name = "Sandwich",
            description = "Veg sandwich",
            price = 80.0,
            categoryId = "C5"
        )

        val cartItem = CartItemModel(product, 2)

        // Wrong on purpose
        assertEquals(3, cartItem.quantity)
    }
}
