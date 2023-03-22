package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import retrofit2.http.GET
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
}