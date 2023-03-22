package com.paraskcd.unitedwalls.repository

import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.data.UnitedWallsDatabaseDao
import com.paraskcd.unitedwalls.model.FavouriteWallsTable
import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import com.paraskcd.unitedwalls.network.WallsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WallsRepository @Inject constructor(private val api: WallsApi, private val dao: UnitedWallsDatabaseDao) {
    private val allWallsDataOrException = DataOrException<Walls, Boolean, Exception>()
    private val wallsByPage = DataOrException<
            List<WallsItem>, Boolean, Exception>()

    suspend fun getWalls(): DataOrException<Walls, Boolean, java.lang.Exception> {
        try {
            allWallsDataOrException.loading = true
            allWallsDataOrException.data = api.getWallsData()

            if (allWallsDataOrException.data.toString().isNotEmpty()) {
                allWallsDataOrException.loading = false
            }
        } catch (ex: Exception) {
            allWallsDataOrException.e = ex
        }

        return allWallsDataOrException
    }

    suspend fun getWallsPerPage(currentPage: Int): DataOrException<
            List<WallsItem>, Boolean, java.lang.Exception> {
        try {
            wallsByPage.loading = true
            wallsByPage.data = api.getWallsDataByPage(currentPage)

            if (wallsByPage.data.toString().isNotEmpty()) {
                wallsByPage.loading = false
            }
        } catch (ex: Exception) {
            wallsByPage.e = ex
        }

        return wallsByPage
    }

    suspend fun favouriteAWallpaper(wall: FavouriteWallsTable) = dao.addFavouriteWall(wall)
    suspend fun unfavouriteAWallpaper(wall: FavouriteWallsTable) = dao.deleteFavouriteWall(wall)
    fun getAllFavouriteWallpapers(): Flow<List<FavouriteWallsTable>> = dao.getAllFavWalls().flowOn(Dispatchers.IO).conflate()
}