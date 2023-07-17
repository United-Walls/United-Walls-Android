package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Uploader
import retrofit2.http.GET
import retrofit2.http.Query

interface UploadersApi {
    @GET("uploaders/walls/queries")
    suspend fun getUploaderWalls(
        @Query("userId") userId: String,
        @Query("page") currentPage: Int
    ): Uploader

    @GET("uploaders/walls/count")
    suspend fun getWallCountUploader(
        @Query("userId") userId: String
    ): Int

    @GET("uploaders/wall")
    suspend fun getUploaderThroughWall(
        @Query("wallId") wallId: String
    ): Uploader

    @GET("uploaders")
    suspend fun getAllUploaders(): List<Uploader>
}