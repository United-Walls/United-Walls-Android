package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Category
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface CategoryApi {
    @GET("category")
    suspend fun getCategoriesData(): ArrayList<Category>

    @GET("category/{category_id}")
    suspend fun getCategoryById(
        @Path("category_id") category_id: String
    ): Category

    @GET("category/walls/queries")
    suspend fun getCategoryWalls(
        @Query("categoryId") categoryId: String,
        @Query("page") page: Int
    ): Category

    @GET("category/walls/count")
    suspend fun getWallCountCategory(
        @Query("categoryId") categoryId: String
    ): Int
}