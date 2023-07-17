package com.paraskcd.unitedwalls.repository

import com.paraskcd.unitedwalls.data.DataAndCount
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.data.UnitedWallsDatabaseDao
import com.paraskcd.unitedwalls.model.Uploader
import com.paraskcd.unitedwalls.network.UploadersApi
import javax.inject.Inject

class UploadersRepository @Inject constructor(private val api: UploadersApi, private val dao: UnitedWallsDatabaseDao) {
    private val uploader = DataOrException<DataAndCount<Uploader, Int>, Boolean, Exception>()
    private val allUploaders = DataOrException<List<Uploader>, Boolean, Exception>()

    suspend fun getUploaderWallsPerPage(userId: String, currentPage: Int): DataOrException<DataAndCount<Uploader, Int>, Boolean, java.lang.Exception> {
        try {
            uploader.loading = true
            uploader.data = DataAndCount(api.getUploaderWalls(userId, currentPage), api.getWallCountUploader(userId))

            if (uploader.data.toString().isNotEmpty()) {
                uploader.loading = false
            }
        } catch (ex: Exception) {
            uploader.e = ex
        }

        return uploader
    }

    suspend fun getAllUploaders(): DataOrException<List<Uploader>, Boolean, java.lang.Exception> {
        try {
            allUploaders.loading = true
            allUploaders.data = api.getAllUploaders()

            if (allUploaders.data.toString().isNotEmpty()) {
                allUploaders.loading = false
            }
        } catch (ex: Exception) {
            allUploaders.e = ex
        }

        return allUploaders
    }

    suspend fun getUploaderByWallId(wallId: String): DataOrException<DataAndCount<Uploader, Int>, Boolean, java.lang.Exception> {
        try {
            uploader.loading = true
            uploader.data = DataAndCount(api.getUploaderThroughWall(wallId))

            if (uploader.data.toString().isNotEmpty()) {
                uploader.loading = false
            }
        } catch (ex: Exception) {
            uploader.e = ex
        }

        return uploader
    }
}