package com.ravirawal.pg_assignment.shared_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUseCase
import com.ravirawal.pg_assignment.data.data_source.SearchPhotosPagingSource
import com.ravirawal.pg_assignment.data.data_source.pagingConfig
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.utils.DELAY_ON_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val DEFAULT_QUERY = ""

class HomeViewModel(private val searchPhotosUseCase: SearchPhotosUseCase) : ViewModel() {

    var photoList = listOf<Photo>()

    var columnCount = MutableLiveData<Int>()

    var searchJob: Job? = null

    private val searchQuery = MutableStateFlow(DEFAULT_QUERY)

    val searchResult: Flow<PagingData<Photo>> = searchQuery.flatMapLatest {
        Pager(pagingConfig) {
            SearchPhotosPagingSource(it, searchPhotosUseCase)
        }.flow.cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        if (searchJob == null || searchJob?.isCompleted == true)
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                delay(DELAY_ON_SEARCH)
                searchQuery.value = query
            }
    }

    private fun currentIndex(currentPhoto: Photo) = photoList.indexOf(currentPhoto)

    fun nextPhoto(currentPhoto: Photo?): Photo? {
        if (currentPhoto == null) {
            return null
        }
        return photoList.getOrNull(currentIndex(currentPhoto) + 1)
    }

    fun prevPhoto(currentPhoto: Photo?): Photo? {
        if (currentPhoto == null) {
            return null
        }
        return photoList.getOrNull(currentIndex(currentPhoto) - 1)
    }
}
