package com.paraskcd.unitedwalls.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pinned_categories_tbl")
data class PinnedCategoriesTable (
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "category_id")
    val categoryId: String
)