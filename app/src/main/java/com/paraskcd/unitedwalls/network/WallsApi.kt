package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Walls
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface WallsApi {
    @GET("walls")
    suspend fun getWallsData(): Walls
}