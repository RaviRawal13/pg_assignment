package com.ravirawal.pg_assignment.data.db.unplashdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravirawal.pg_assignment.data.db.remoteKeysDb.RemoteKeys
import com.ravirawal.pg_assignment.data.db.remoteKeysDb.RemoteKeysDao

@Database(
    entities = [UnsplashPhotoEntity::class, RemoteKeys::class],
    exportSchema = false,
    version = 1
)
abstract class UnsplashDatabase(): RoomDatabase() {

    abstract fun getDao(): UnsplashDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}
