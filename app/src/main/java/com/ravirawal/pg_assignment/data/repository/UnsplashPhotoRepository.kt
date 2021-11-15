package com.ravirawal.pg_assignment.data.repository

import com.ravirawal.pg_assignment.data.Output
import com.ravirawal.pg_assignment.data.remote.ApiService
import com.ravirawal.pg_assignment.data.remote.model.Photos
import com.ravirawal.pg_assignment.data.repository.UnsplashPhotoRepository.UnsplashPhotoRepositoryImpl

/**
 * UnsplashPhotoRepository is an interface data layer to handle communication
 * with any data source such as Server or local database.
 */
interface UnsplashPhotoRepository {
    suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Output<List<Photos>>

    class UnsplashPhotoRepositoryImpl(
        private val apiService: ApiService
    ) : UnsplashPhotoRepository {

        override suspend fun searchPhotos(
            query: String,
            pageNumber: Int,
            pageSize: Int
        ): Output<List<Photos>> {
            val response = apiService.searchPhotos(query, pageNumber, pageSize)
            return if (response.isSuccessful) {
                response.body()?.results?.let {
                    Output.Success(it.filterNotNull())
                } ?: Output.Error("No network connected!")
            } else {
                Output.Error("No network connected!")
            }
        }
    }
}
