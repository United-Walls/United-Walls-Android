package com.paraskcd.unitedwalls.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.unitedwalls.data.UnitedWallsDatabase
import com.paraskcd.unitedwalls.data.UnitedWallsDatabaseDao
import com.paraskcd.unitedwalls.network.CategoryApi
import com.paraskcd.unitedwalls.network.WallsApi
import com.paraskcd.unitedwalls.repository.CategoryRepository
import com.paraskcd.unitedwalls.repository.WallsRepository
import com.paraskcd.unitedwalls.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesWallpapersRepository(api: WallsApi, dao: UnitedWallsDatabaseDao) = WallsRepository(api, dao)

    @Singleton
    @Provides
    fun providesWallpapersApi(): WallsApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build().create(WallsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesCateogoriesRepository(api: CategoryApi, dao: UnitedWallsDatabaseDao) = CategoryRepository(api, dao)

    @Singleton
    @Provides
    fun providesCategoriesApi(): CategoryApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CategoryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepoDBDao(unitedWallsDatabase: UnitedWallsDatabase): UnitedWallsDatabaseDao = unitedWallsDatabase.unitedWallsDao()

    @Singleton
    @Provides
    fun provideRepoDB(@ApplicationContext context: Context): UnitedWallsDatabase = Room.databaseBuilder(context, UnitedWallsDatabase::class.java, "united_walls_db").fallbackToDestructiveMigration().build()
}