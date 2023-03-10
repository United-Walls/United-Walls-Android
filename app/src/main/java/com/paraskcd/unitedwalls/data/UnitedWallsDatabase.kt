package com.paraskcd.unitedwalls.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paraskcd.unitedwalls.model.FavouriteWallsTable
import com.paraskcd.unitedwalls.utils.UUIDConverter

@Database(
    entities = [FavouriteWallsTable::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UUIDConverter::class)
abstract class UnitedWallsDatabase: RoomDatabase() {
    abstract fun unitedWallsDao(): UnitedWallsDatabaseDao
}