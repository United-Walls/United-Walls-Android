package com.paraskcd.unitedwalls.di

import com.paraskcd.unitedwalls.network.CategoryApi
import com.paraskcd.unitedwalls.network.WallsApi
import com.paraskcd.unitedwalls.repository.CategoryRepository
import com.paraskcd.unitedwalls.repository.WallsRepository
import com.paraskcd.unitedwalls.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesWallpapersRepository(api: WallsApi) = WallsRepository(api)

    @Singleton
    @Provides
    fun providesWallpapersApi(): WallsApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build().create(WallsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesCateogoriesRepository(api: CategoryApi) = CategoryRepository(api)

    @Singleton
    @Provides
    fun providesCategoriesApi(): CategoryApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CategoryApi::class.java)
    }
}