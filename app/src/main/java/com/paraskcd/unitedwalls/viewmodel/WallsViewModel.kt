package com.paraskcd.unitedwalls.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import com.paraskcd.unitedwalls.repository.WallsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallsViewModel @Inject constructor(private val wallsRepository: WallsRepository): ViewModel() {
    val wallsData: MutableState<DataOrException<Walls, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))

    val wallByIdData: MutableState<DataOrException<WallsItem, Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception(""))
    )

    private val _walls = MutableLiveData<Walls>()
    private val _loadingWalls = MutableLiveData<Boolean>(true)

    private val _wallById = MutableLiveData<WallsItem>()
    private val _loadingWallById = MutableLiveData<Boolean>(true)

    val walls: LiveData<Walls>
        get() = _walls
    val loadingWalls: LiveData<Boolean>
        get() = _loadingWalls

    val wallById: LiveData<WallsItem>
        get() = _wallById
    val loadingWallById: MutableLiveData<Boolean>
        get() = _loadingWallById

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getWallsData()
        }
    }

    fun getWallsData() {
        viewModelScope.launch {
            wallsData.value.loading = true
            wallsData.value = wallsRepository.getWalls()

            if(wallsData.value.data.toString().isNotEmpty()) {
                wallsData.value.loading = false
                _loadingWalls.value = false
                _walls.value = wallsData.value.data
                Log.d("Walls", walls.value.toString())
            }
        }
    }

    fun getWallById(wallId: String) {
        viewModelScope.launch {
            wallByIdData.value.loading = true
            wallByIdData.value = wallsRepository.getWallById(wallId)

            if (wallByIdData.value.data.toString().isNotEmpty()) {
                wallByIdData.value.loading = false
                _loadingWallById.value = false
                _wallById.value = wallByIdData.value.data
            }
        }
    }
}