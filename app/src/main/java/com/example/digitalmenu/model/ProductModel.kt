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
        if (productId == "1") return R.drawable.pizza
        if (productId == "2") return R.drawable.burger
        if (productId == "3") return R.drawable.momo
        if (productId == "4") return R.drawable.chocolava

        val lowerName = name.lowercase()
        return when {
            lowerName.contains("pizza") -> R.drawable.pizza
            lowerName.contains("burger") -> R.drawable.burger
            lowerName.contains("momo") -> R.drawable.momo
            lowerName.contains("chocolava") || lowerName.contains("cake") || lowerName.contains("dessert") -> R.drawable.chocolava
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


