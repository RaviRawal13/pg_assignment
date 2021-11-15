package com.ravirawal.pg_assignment.data.usecases

import com.ravirawal.pg_assignment.data.Output
import com.ravirawal.pg_assignment.data.remote.DEFAULT_PAGE_NUMBER
import com.ravirawal.pg_assignment.data.remote.DEFAULT_PER_PAGE_SIZE
import com.ravirawal.pg_assignment.data.remote.model.Photos
import com.ravirawal.pg_assignment.data.repository.UnsplashPhotoRepository
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.utils.default

/**
 * A use-case to search photos from Unsplash API.
 */
class SearchPhotosUseCase(private val repository: UnsplashPhotoRepository) {
    suspend operator fun invoke(
        query: String,
        pageNum: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PER_PAGE_SIZE
    ): List<Photo> {
        val response =
            repository.searchPhotos(
                query = query,
                pageNumber = pageNum,
                pageSize = pageSize
            )

        val photoUiList: List<Photo> = when (response) {
            is Output.Success -> {
                val items = response.data
                items.map { item ->
                    Photo(
                        photoId = item.id.default(),
                        full = item.urls?.full.default(),
                        small = item.urls?.small.default(),
                        thumb = item.urls?.thumb.default()
                    )
                }
            }
            is Output.Error -> {
                emptyList()
            }
        }

        return photoUiList
    }
}
