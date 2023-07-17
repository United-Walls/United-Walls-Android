package com.paraskcd.unitedwalls.repository

import com.paraskcd.unitedwalls.data.DataAndCount
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.data.UnitedWallsDatabaseDao
import com.paraskcd.unitedwalls.model.Category
import com.paraskcd.unitedwalls.network.CategoryApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val api: CategoryApi, private val dao: UnitedWallsDatabaseDao) {
    private val allCategoriesDataOrException = DataOrException<ArrayList<Category>, Boolean, Exception>()
    private val categoryDataOrException = DataOrException<Category, Boolean, Exception>()
    private val selectedCategoryDataOrException = DataOrException<DataAndCount<Category, Int>, Boolean, Exception>()

    suspend fun getCategories(): DataOrException<ArrayList<Category>, Boolean, java.lang.Exception> {
        try {
            allCategoriesDataOrException.loading = true
            allCategoriesDataOrException.data = api.getCategoriesData()

            if (allCategoriesDataOrException.data.toString().isNotEmpty()) {
                allCategoriesDataOrException.loading = false
            }
        } catch (ex: Exception) {
            allCategoriesDataOrException.e = ex
        }

        return allCategoriesDataOrException
    }

    suspend fun getCategoryById(categoryId: String): DataOrException<Category, Boolean, Exception> {
        try {
            categoryDataOrException.loading = true
            categoryDataOrException.data = api.getCategoryById(categoryId)

            if (categoryDataOrException.data.toString().isNotEmpty()) {
                categoryDataOrException.loading = false
            }
        } catch (ex: Exception) {
            categoryDataOrException.e = ex
        }

        return categoryDataOrException
    }

    suspend fun getCategoryWallsPerPage(categoryId: String, currentPage: Int): DataOrException<DataAndCount<Category, Int>, Boolean, Exception> {
        try {
            selectedCategoryDataOrException.loading = true
            selectedCategoryDataOrException.data = DataAndCount(api.getCategoryWalls(categoryId, currentPage), api.getWallCountCategory(categoryId))

            if (selectedCategoryDataOrException.data.toString().isNotEmpty()) {
                selectedCategoryDataOrException.loading = false
            }
        } catch (ex: Exception) {
            selectedCategoryDataOrException.e = ex
        }

        return selectedCategoryDataOrException
    }
}