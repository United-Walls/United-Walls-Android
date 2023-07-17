package com.paraskcd.unitedwalls.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.unitedwalls.data.DataAndCount
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.model.Category
import com.paraskcd.unitedwalls.model.Uploader
import com.paraskcd.unitedwalls.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository): ViewModel() {
    private val currentPage: MutableState<Int> = mutableStateOf(0)
    var selectedWallIndex: MutableState<Int> = mutableStateOf(0)

    private val categoriesData: MutableState<DataOrException<ArrayList<Category>, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val categoryByIdData: MutableState<DataOrException<Category, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val categoryData: MutableState<DataOrException<DataAndCount<Category, Int>, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val _selectedCategory = MutableLiveData<Category>()
    private val _selectedCategoryWallCount = MutableLiveData<Int>()
    private val _loadingWalls = MutableLiveData<Boolean>()

    private val _categories = MutableLiveData<ArrayList<Category>>()
    private val _loadingCategories = MutableLiveData(true)

    private val _categoryById = MutableLiveData<Category>()
    private val _loadingCategoryById = MutableLiveData(true)

    val loadingWalls: LiveData<Boolean>
        get() = _loadingWalls

    val selectedCategory: LiveData<Category>
        get() = _selectedCategory

    val selectedCategoryWallCount: LiveData<Int>
        get() = _selectedCategoryWallCount

    val categories: LiveData<ArrayList<Category>>
        get() = _categories

    val loadingCategories: LiveData<Boolean>
        get() = _loadingCategories

    val categoryById: LiveData<Category>
        get() = _categoryById

    val loadingCategoryById: MutableLiveData<Boolean>
        get() = _loadingCategoryById

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesData()
        }
    }

    fun selectWallIndex(index: Int) {
        selectedWallIndex.value = index
    }

    fun resetPage() {
        currentPage.value = 0
    }

    private fun getCategoriesData() {
        viewModelScope.launch {
            categoriesData.value.loading = true
            categoriesData.value = categoryRepository.getCategories()

            if (categoriesData.value.data.toString().isNotEmpty()) {
                categoriesData.value.loading = false
                _loadingCategories.value = false
                _categories.value = categoriesData.value.data
                Log.d("Categories", _categories.value.toString())
            }
        }
    }

    fun getCategoryById(categoryId: String) {
        viewModelScope.launch {
            categoryByIdData.value.loading = true
            categoryByIdData.value = categoryRepository.getCategoryById(categoryId)

            if (categoryByIdData.value.data.toString().isNotEmpty()) {
                categoryByIdData.value.loading = false
                _loadingCategoryById.value = false
                _categoryById.value = categoryByIdData.value.data
            }
        }
    }

    fun clearCategoryById() {
        _loadingCategoryById.value = true
        _categoryById.value = null
    }

    fun getCategoryWallsData(categoryId: String) {
        viewModelScope.launch {
            _selectedCategory.value = null
            categoryData.value.loading = true
            categoryData.value = categoryRepository.getCategoryWallsPerPage(categoryId, 0)
            if(categoryData.value.data.toString().isNotEmpty()) {
                categoryData.value.loading = false
                _selectedCategory.value = categoryData.value.data!!.data
                _selectedCategoryWallCount.value = categoryData.value.data!!.count
                Log.d("Selected Category", categoryData.value.data?.data.toString())
                categoryData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false
                currentPage.value += 1
            }
        }
    }

    fun getMoreCategoryWallsData(categoryId: String) {
        viewModelScope.launch {
            Log.d("Wallss", currentPage.value.toString())
            categoryData.value.loading = true
            categoryData.value = categoryRepository.getCategoryWallsPerPage(categoryId, currentPage.value)
            if(categoryData.value.data.toString().isNotEmpty()) {
                categoryData.value.loading = false
                _selectedCategory.value = Category(__v = _selectedCategory.value!!.__v, _id = _selectedCategory.value!!._id, name = _selectedCategory.value!!.name, walls = _selectedCategory.value!!.walls.plus(
                    categoryData.value.data!!.data!!.walls))
                Log.d("Selected Category More Data", _selectedCategory.value!!.walls.toString())
                categoryData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false
                currentPage.value += 1
            }
        }
    }
}