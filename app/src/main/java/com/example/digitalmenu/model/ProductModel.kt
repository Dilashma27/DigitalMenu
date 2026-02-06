package com.example.digitalmenu.model

import com.example.digitalmenu.R

data class ProductModel(
    var productId: String="",
    var name: String="",
    var description: String="",
    var price: Double= 0.0,
    var categoryId : String = "",
    val image: String? = null
){
    fun getImageResource(): Int {
        return when (productId) {
            "1" -> R.drawable.pizza
            "2" -> R.drawable.burger
            "3" -> R.drawable.momo
            "4" -> R.drawable.chocolava
            else -> R.drawable.pizza
        }
    }

    fun toMap() :Map<String,Any?>{
        return mapOf(
            "productId" to productId,
            "name" to name,
            "description" to description,
            "price" to price,
            "categoryId" to categoryId,
            "image" to image
        )
    }
}


