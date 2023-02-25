package com.paraskcd.unitedwalls.model

data class Category(
    val __v: Int,
    val _id: String,
    val name: String,
    val walls: List<WallsItem>
)