package com.example.digitalmenu.model

data class ProductModel(
    var productId: String="",
    var name: String="",
    var description: String="",
    var price: Double= 0.0,
    var categoryId : String = "",
    var image: String =""
){
    fun toMap() :Map<String,Any?>{
        return mapOf(
            "productId" to productId,
            "name" to name,
            "description" to description,
            "price" to price,
            "categoryId" to categoryId
        )
    }
}


