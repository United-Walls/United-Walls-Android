package com.paraskcd.unitedwalls.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.model.Category
import com.paraskcd.unitedwalls.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository): ViewModel() {
    private val categoriesData: MutableState<DataOrException<ArrayList<Category>, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val categoryByIdData: MutableState<DataOrException<Category, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val _categories = MutableLiveData<ArrayList<Category>>()
    private val _loadingCategories = MutableLiveData(true)

    private val _categoryById = MutableLiveData<Category>()
    private val _loadingCategoryById = MutableLiveData(true)

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
}