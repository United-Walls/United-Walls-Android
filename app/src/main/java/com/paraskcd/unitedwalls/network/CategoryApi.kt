package com.paraskcd.unitedwalls.network

import com.paraskcd.unitedwalls.model.Category
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface CategoryApi {
    @GET("category")
    suspend fun getCategoriesData(): ArrayList<Category>

    @GET("category/{category_id}")
    suspend fun getCategoryById(
        @Path("category_id") category_id: String
    ): Category
}