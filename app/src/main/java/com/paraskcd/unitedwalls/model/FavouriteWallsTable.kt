package com.paraskcd.unitedwalls.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "fav_walls_tbl")
data class FavouriteWallsTable(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "wallpaper_id")
    val wallpaperId: String
)