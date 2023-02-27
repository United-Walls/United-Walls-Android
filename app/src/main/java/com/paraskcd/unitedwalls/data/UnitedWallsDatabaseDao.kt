package com.paraskcd.unitedwalls.data

import androidx.room.*
import com.paraskcd.unitedwalls.model.FavouriteWallsTable
import com.paraskcd.unitedwalls.model.PinnedCategoriesTable
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitedWallsDatabaseDao {
    @Query("SELECT * FROM fav_walls_tbl")
    fun getAllFavWalls(): Flow<List<FavouriteWallsTable>>

    @Query("SELECT * FROM pinned_categories_tbl")
    fun getAllPinnedCategories(): Flow<List<PinnedCategoriesTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteWall(wall: FavouriteWallsTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPinnedCategory(category: PinnedCategoriesTable)

    @Delete
    suspend fun deleteFavouriteWall(wall: FavouriteWallsTable)
    
    @Delete
    suspend fun deletePinnedCategory(category: PinnedCategoriesTable)
}