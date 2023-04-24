package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WallsApi {
    @GET("walls")
    suspend fun getWallsData(): Walls

    @GET("walls/queries")
    suspend fun getWallsDataByPage(
        @Query("page") currentPage: Int
    ): List<WallsItem>

    @GET("walls/addDownloaded")
    suspend fun addDownloaded(
        @Query("wallId") wallId: String
    ): WallsItem

    @GET("walls/addFav")
    suspend fun addFavourite(
        @Query("wallId") wallId: String
    ): WallsItem

    @GET ("walls/removeFav")
    suspend fun removeFavourite(
        @Query("wallId") wallId: String
    ): WallsItem

    @GET("walls/mostFavourited")
    suspend fun getAllMostFavouritedWalls(): List<WallsItem>

    @GET("walls/mostDownloaded")
    suspend fun getAllMostDownloadedWalls(): List<WallsItem>

    @GET("walls/wallOfDay")
    suspend fun wallOfDay(): WallsItem

    @GET("walls/{wallId}")
    suspend fun getWallById(
        @Path("wallId") wallId: String
    ): WallsItem
}