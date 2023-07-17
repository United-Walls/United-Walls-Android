package com.paraskcd.unitedwalls.model

data class Uploader(
    val __v: Int,
    val _id: String,
    val userID: Int,
    val username: String,
    val avatar_file_url: String?,
    val avatar_uuid: String?,
    val avatar_mime_type: String?,
    var walls: List<WallsItem>
)
