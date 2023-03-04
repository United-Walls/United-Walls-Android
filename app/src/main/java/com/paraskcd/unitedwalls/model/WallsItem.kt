package com.paraskcd.unitedwalls.model

data class WallsItem(
    val __v: Int,
    val _id: String,
    val category: String,
    val createdAt: String,
    val file_id: String,
    val file_name: String,
    val file_url: String?,
    val mime_type: String,
    val updatedAt: String,
    val addedBy: String?
)