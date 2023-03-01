package com.paraskcd.unitedwalls.data

import androidx.room.*
import com.paraskcd.unitedwalls.model.FavouriteWallsTable
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitedWallsDatabaseDao {
    @Query("SELECT * FROM fav_walls_tbl")
    fun getAllFavWalls(): Flow<List<FavouriteWallsTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteWall(wall: FavouriteWallsTable)

    @Delete
    suspend fun deleteFavouriteWall(wall: FavouriteWallsTable)
}