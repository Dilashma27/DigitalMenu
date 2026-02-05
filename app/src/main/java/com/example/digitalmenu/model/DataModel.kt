package com.example.digitalmenu.model

data class DataModel(
    val id: Int,
val name: String,
val price: Double,
val image: Int,
val isFavorite: Boolean = false
)

