package com.ravirawal.pg_assignment.data.remote

import com.ravirawal.pg_assignment.data.remote.model.SearchPhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(API_SEARCH_PHOTOS)
    suspend fun searchPhotos(
        @Query(QUERY) query: String,
        @Query(PAGE) page: Int = DEFAULT_PAGE_NUMBER,
        @Query(PER_PAGE) numOfPhotos: Int = DEFAULT_PER_PAGE_SIZE,
    ): Response<SearchPhotosResponse>
}
