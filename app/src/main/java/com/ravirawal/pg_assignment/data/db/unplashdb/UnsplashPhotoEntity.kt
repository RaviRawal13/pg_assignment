package com.ravirawal.pg_assignment.data.db.unplashdb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ravirawal.pg_assignment.utils.default

@Entity(tableName = "unsplash")
data class UnsplashPhotoEntity(
    @PrimaryKey
    val id: String,
    val regularUrl: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().default(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(regularUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnsplashPhotoEntity> {
        override fun createFromParcel(parcel: Parcel): UnsplashPhotoEntity {
            return UnsplashPhotoEntity(parcel)
        }

        override fun newArray(size: Int): Array<UnsplashPhotoEntity?> {
            return arrayOfNulls(size)
        }
    }

}
