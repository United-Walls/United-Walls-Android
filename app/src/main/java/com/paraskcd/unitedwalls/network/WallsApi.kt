package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface WallsApi {
    @GET("walls")
    suspend fun getWallsData(): Walls

    @GET("walls/{wall_id}")
    suspend fun getWallById(
        @Path("wall_id") wall_id: String
    ): WallsItem
}