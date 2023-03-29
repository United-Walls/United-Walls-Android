package com.paraskcd.unitedwalls.viewmodel

import android.app.PendingIntent.getActivity
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.unitedwalls.data.DataOrException
import com.paraskcd.unitedwalls.model.FavouriteWallsTable
import com.paraskcd.unitedwalls.model.Walls
import com.paraskcd.unitedwalls.model.WallsItem
import com.paraskcd.unitedwalls.repository.WallsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class WallsViewModel @Inject constructor(private val wallsRepository: WallsRepository, private val wallpaperManager: WallpaperManager): ViewModel() {
    private val wallsDataCount: MutableState<DataOrException<Walls, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))
    private val wallsData: MutableState<DataOrException<
            List<WallsItem>, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))
    private val currentPage: MutableState<Int> = mutableStateOf(0)

    private val _walls = MutableLiveData<List<WallsItem>>()
    private val _loadingWalls = MutableLiveData(true)
    private val _totalWalls = MutableLiveData(0)

    private val _favouriteWalls = MutableStateFlow<List<FavouriteWallsTable>>(emptyList())
    val favouriteWalls = _favouriteWalls.asStateFlow()

    var favouritePopulatedWallsStore: MutableList<WallsItem> = mutableListOf()

    var selectedWallIndex: MutableState<Int> = mutableStateOf(0)
    var selectedFavouriteWallIndex: MutableState<Int> = mutableStateOf(0)

    val walls: LiveData<List<WallsItem>>
        get() = _walls
    val loadingWalls: LiveData<Boolean>
        get() = _loadingWalls
    val totalWalls: LiveData<Int>
        get() = _totalWalls

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getWallsData()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getFavouriteWalls()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getWallsCount()
        }
    }

    private fun getWallsCount() {
        viewModelScope.launch {
            wallsDataCount.value.loading = true
            wallsDataCount.value = wallsRepository.getWalls()
            if(wallsDataCount.value.data.toString().isNotEmpty()) {
                wallsDataCount.value.loading = false
                _totalWalls.value = wallsDataCount.value.data?.size
                wallsData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
            }
        }
    }

    fun resetPage() {
        currentPage.value = 0
    }

    fun getWallsData() {
        viewModelScope.launch {
            _walls.value = emptyList()
            wallsData.value.loading = true
            wallsData.value = wallsRepository.getWallsPerPage(0)
            if(wallsData.value.data.toString().isNotEmpty()) {
                wallsData.value.loading = false
                _walls.value = wallsData.value.data
                Log.d("Wallss Size", walls.value!!.size.toString())
                wallsData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false

            }
        }
    }

    fun getMoreData() {
        viewModelScope.launch {
            currentPage.value += 1
            Log.d("Wallss", currentPage.value.toString())
            wallsData.value.loading = true
            wallsData.value = wallsRepository.getWallsPerPage(currentPage.value)
            if(wallsData.value.data.toString().isNotEmpty()) {
                wallsData.value.loading = false
                var wallsNewData: List<WallsItem> = walls.value!!.plus(wallsData.value.data!!)
                _walls.postValue(wallsNewData)
                Log.d("Wallss Size", walls.value!!.size.toString())
                wallsData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false
                currentPage.value += 1
            }
        }
    }

    fun selectWallIndex(index: Int) {
        selectedWallIndex.value = index
    }

    fun selectFavouriteWallIndex(index: Int) {
        selectedFavouriteWallIndex.value = index
    }

    private fun getFavouriteWalls() {
        viewModelScope.launch {
            wallsRepository.getAllFavouriteWallpapers().distinctUntilChanged().collect {
                listOfFavouriteWalls ->
                if (listOfFavouriteWalls.isEmpty()) {
                    _favouriteWalls.value = emptyList()
                } else {
                    Log.d("Favourite Walls", listOfFavouriteWalls.toString())
                    _favouriteWalls.value = listOfFavouriteWalls
                }
            }
        }
    }

    fun getPopulatedFavouriteWalls() {
        viewModelScope.launch {
            favouritePopulatedWallsStore = mutableListOf()
            if (walls.value != null) {
                for (wall in favouriteWalls.value) {
                    for (wallItem in walls.value!!) {
                        if (wallItem._id == wall.wallpaperId) {
                            favouritePopulatedWallsStore.add(wallItem)
                        }
                    }
                }
            }
        }
    }

    fun addWallToFavourites(wallId: String) {
        viewModelScope.launch {
            wallsRepository.favouriteAWallpaper(FavouriteWallsTable(wallpaperId = wallId))
        }
    }

    fun removeWallFromFavourites(wall: FavouriteWallsTable) {
        viewModelScope.launch {
            wallsRepository.unfavouriteAWallpaper(wall)
        }
    }

    fun setAsWallpaper(context: Context, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                startActivity(context, wallpaperManager.getCropAndSetWallpaperIntent(getImageUri(context, bitmap)), null)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getImageUri(context: Context, bitmap: Bitmap?): Uri {
        val contentResolver = context.contentResolver
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver, bitmap, System.currentTimeMillis().toString(), null
        )
        return Uri.parse(path)
    }
}