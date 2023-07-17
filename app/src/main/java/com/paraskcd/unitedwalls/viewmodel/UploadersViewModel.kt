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
import com.paraskcd.unitedwalls.model.Uploader
import com.paraskcd.unitedwalls.model.WallsItem
import com.paraskcd.unitedwalls.repository.UploadersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UploadersViewModel @Inject constructor(private val uploadersRepository: UploadersRepository): ViewModel() {
    private val allUploadersData: MutableState<DataOrException<List<Uploader>, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, null))
    private val uploaderData: MutableState<DataOrException<DataAndCount<Uploader, Int>, Boolean, Exception>> =
        mutableStateOf(DataOrException(DataAndCount(null, null), true, null))
    private val currentPage: MutableState<Int> = mutableStateOf(0)
    var selectedWallIndex: MutableState<Int> = mutableStateOf(0)

    private val _loadingWalls = MutableLiveData<Boolean>()
    private val _loadingUploaders = MutableLiveData<Boolean>()
    private val _uploaders = MutableLiveData<List<Uploader>>()
    private val _selectedUploader = MutableLiveData<Uploader?>()
    private val _totalWallsOfUploader = MutableLiveData<Int>()

    val uploaders: LiveData<List<Uploader>>
        get() = _uploaders

    val selectedUploader: LiveData<Uploader?>
        get() = _selectedUploader

    val totalWallsOfUploader: LiveData<Int>
        get() = _totalWallsOfUploader

    val loadingWalls: LiveData<Boolean>
        get() = _loadingWalls

    val loadingUploaders: LiveData<Boolean>
        get() = _loadingUploaders

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllUploaders()
        }
    }

    fun selectWallIndex(index: Int) {
        selectedWallIndex.value = index
    }

    fun resetPage() {
        currentPage.value = 0
    }

    fun getAllUploaders() {
        viewModelScope.launch {
            _uploaders.value = emptyList()
            allUploadersData.value.loading = true
            allUploadersData.value = uploadersRepository.getAllUploaders()

            if (allUploadersData.value.data.toString().isNotEmpty()) {
                allUploadersData.value.loading = false
                _uploaders.value = allUploadersData.value.data
                Log.d("Uploaderss Size", _uploaders.value.toString())
                uploaderData.value.e?.localizedMessage?.let {
                    Log.d("UploadersError", it)
                }
                _loadingUploaders.value = false
            }
        }
    }

    fun getUploaderWallsData(userId: String) {
        viewModelScope.launch {
            _selectedUploader.value = null
            uploaderData.value.loading = true
            uploaderData.value = uploadersRepository.getUploaderWallsPerPage(userId, 0)
            if(uploaderData.value.data.toString().isNotEmpty()) {
                uploaderData.value.loading = false
                _selectedUploader.value = uploaderData.value.data!!.data
                _totalWallsOfUploader.value = uploaderData.value.data!!.count
                Log.d("Selected Uploader", uploaderData.value.data?.data.toString())
                uploaderData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false
                currentPage.value += 1
            }
        }
    }

    fun clearCategoryById() {
        _loadingWalls.value = true
        _selectedUploader.value = null
    }

    fun getMoreUploaderWallsData(userId: String) {
        viewModelScope.launch {
            Log.d("Wallss", currentPage.value.toString())
            uploaderData.value.loading = true
            uploaderData.value = uploadersRepository.getUploaderWallsPerPage(userId, currentPage.value)
            if(uploaderData.value.data.toString().isNotEmpty()) {
                uploaderData.value.loading = false
                _selectedUploader.value = Uploader(__v = _selectedUploader.value!!.__v, _id = _selectedUploader.value!!._id, userID = _selectedUploader.value!!.userID, username = _selectedUploader.value!!.username, avatar_file_url = _selectedUploader.value!!.avatar_file_url, avatar_mime_type = _selectedUploader.value!!.avatar_mime_type, avatar_uuid = _selectedUploader.value!!.avatar_uuid, walls = _selectedUploader.value!!.walls.plus(
                    uploaderData.value.data!!.data!!.walls))
                Log.d("Selected Uploader More Data", _selectedUploader.value!!.walls.toString())
                uploaderData.value.e?.localizedMessage?.let {
                    Log.d("WallsError", it)
                }
                _loadingWalls.value = false
                currentPage.value += 1
            }
        }
    }
}