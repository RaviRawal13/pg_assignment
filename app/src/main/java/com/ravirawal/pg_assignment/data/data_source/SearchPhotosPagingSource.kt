package com.ravirawal.pg_assignment.data.data_source

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ravirawal.pg_assignment.data.Output
import com.ravirawal.pg_assignment.data.remote.DEFAULT_PAGE_NUMBER
import com.ravirawal.pg_assignment.data.remote.DEFAULT_PER_PAGE_SIZE
import com.ravirawal.pg_assignment.data.remote.PREFETCH_DISTANCE
import com.ravirawal.pg_assignment.data.remote.model.Photos
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUseCase
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.utils.default
import retrofit2.HttpException
import java.io.IOException

val pagingConfig = PagingConfig(
    pageSize = DEFAULT_PER_PAGE_SIZE,
    prefetchDistance = PREFETCH_DISTANCE,
    enablePlaceholders = true,
)

class SearchPhotosPagingSource(
    private val query: String,
    private val searchPhotosUseCase: SearchPhotosUseCase
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val pageIndex = params.key ?: DEFAULT_PAGE_NUMBER
        return try {

            val photos: List<Photo> = searchPhotosUseCase.invoke(
                query = query, pageNum = pageIndex, pageSize = DEFAULT_PER_PAGE_SIZE,
            )

            val nextKey =
                if (photos.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / DEFAULT_PER_PAGE_SIZE)
                }

            LoadResult.Page(
                data = photos,
                prevKey = if (pageIndex == DEFAULT_PAGE_NUMBER) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

//    override val keyReuseSupported: Boolean
//        get() = true

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}