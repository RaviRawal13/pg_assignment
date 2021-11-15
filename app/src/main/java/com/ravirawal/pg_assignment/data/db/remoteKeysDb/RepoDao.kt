package com.ravirawal.pg_assignment.data.db.remoteKeysDb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ravirawal.pg_assignment.data.db.unplashdb.UnsplashPhotoEntity

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<UnsplashPhotoEntity>)

    @Query("SELECT * FROM unsplash")
    fun reposByName(queryString: String): PagingSource<Int, UnsplashPhotoEntity>

    @Query("DELETE FROM unsplash")
    suspend fun clearRepos()
}
