package com.paraskcd.unitedwalls.repository

import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import com.paraskcd.unitedwalls.network.WallsApi
import javax.inject.Inject

class WallsRepository @Inject constructor(private val api: WallsApi) {
    private val allWallsDataOrException = DataOrException<Walls, Boolean, Exception>()
    private val wallDataOrException = DataOrException<WallsItem, Boolean, Exception>()

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

    suspend fun getWallById(wallId: String): DataOrException<WallsItem, Boolean, java.lang.Exception> {
        try {
            wallDataOrException.loading = true
            wallDataOrException.data = api.getWallById(wallId)

            if (wallDataOrException.data.toString().isNotEmpty()) {
                wallDataOrException.loading = false
            }
        } catch (ex: Exception) {
            wallDataOrException.e = ex
        }

        return wallDataOrException
    }
}