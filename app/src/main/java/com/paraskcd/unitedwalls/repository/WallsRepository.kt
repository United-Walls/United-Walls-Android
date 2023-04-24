package com.paraskcd.unitedwalls.repository

import android.util.Log
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
    private val wallsByPage = DataOrException<List<WallsItem>, Boolean, Exception>()
    private val mostDownloadedWalls = DataOrException<List<WallsItem>, Boolean, java.lang.Exception>()
    private val mostFavouritedWalls = DataOrException<List<WallsItem>, Boolean, java.lang.Exception>()
    private val wallOfDay = DataOrException<WallsItem, Boolean, java.lang.Exception>()
    private val wallById = DataOrException<WallsItem, Boolean, java.lang.Exception>()

    suspend fun getWallOfDay(): DataOrException<WallsItem, Boolean, java.lang.Exception> {
        try {
            wallOfDay.loading = true
            wallOfDay.data = api.wallOfDay()

            if (wallOfDay.data.toString().isNotEmpty()) {
                wallOfDay.loading = false
            }
        } catch (ex: Exception) {
            wallOfDay.e = ex
        }

        return wallOfDay
    }

    suspend fun getWallById(wallId: String): DataOrException<WallsItem, Boolean, java.lang.Exception> {
        try {
            wallById.loading = true
            wallById.data = api.getWallById(wallId)

            if (wallById.data.toString().isNotEmpty()) {
                wallById.loading = false
            }
        } catch (ex: Exception) {
            wallById.e = ex
        }

        return wallById
    }

    suspend fun addToServer(wallId: String, apiCall: String) {
        try {
            if (apiCall == "addDownloaded") {
                api.addDownloaded(wallId)
            }

            if (apiCall == "addFav") {
                api.addFavourite(wallId)
            }

            if (apiCall == "removeFav") {
                api.removeFavourite(wallId)
            }
        } catch (ex: Exception) {
            Log.d("Error - ", "Couldn't add to server because - \n $ex")
        }
    }

    suspend fun getMostDownloadedWalls(): DataOrException<List<WallsItem>, Boolean, java.lang.Exception> {
        try {
            mostDownloadedWalls.loading = true
            mostDownloadedWalls.data = api.getAllMostDownloadedWalls()

            if (mostDownloadedWalls.data.toString().isNotEmpty()) {
                mostDownloadedWalls.loading = false
            }
        } catch (ex: Exception) {
            mostDownloadedWalls.e = ex
        }

        return mostDownloadedWalls
    }

    suspend fun getMostFavouritedWalls(): DataOrException<List<WallsItem>, Boolean, java.lang.Exception> {
        try {
            mostFavouritedWalls.loading = true
            mostFavouritedWalls.data = api.getAllMostFavouritedWalls()

            if (mostFavouritedWalls.data.toString().isNotEmpty()) {
                mostFavouritedWalls.loading = false
            }
        } catch (ex: Exception) {
            mostFavouritedWalls.e = ex
        }

        return mostFavouritedWalls
    }

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

    suspend fun getWallsPerPage(currentPage: Int): DataOrException<List<WallsItem>, Boolean, java.lang.Exception> {
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