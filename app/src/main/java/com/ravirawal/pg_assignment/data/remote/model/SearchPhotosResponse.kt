package com.ravirawal.pg_assignment.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

typealias Photos = SearchPhotosResponse.Result

data class SearchPhotosResponse(
    @SerializedName("results")
    val results: List<Result?>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?
) {
    data class Result(
//        @SerializedName("blur_hash")
//        val blurHash: String?,
//        @SerializedName("color")
//        val color: String?,
//        @SerializedName("height")
//        val height: Int?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("urls")
        val urls: Urls?,
//        @SerializedName("width")
//        val width: Int?,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
//            parcel.readString(),
//            parcel.readString(),
//            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readParcelable(Urls::class.java.classLoader),
//            parcel.readValue(Int::class.java.classLoader) as? Int
        )

        data class Urls(
            @SerializedName("full")
            val full: String?,
//            @SerializedName("raw")
//            val raw: String?,
//            @SerializedName("regular")
//            val regular: String?,
            @SerializedName("small")
            val small: String?,
            @SerializedName("thumb")
            val thumb: String?
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                parcel.readString(),
//                parcel.readString(),
//                parcel.readString(),
                parcel.readString(),
                parcel.readString()
            )

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(full)
//                parcel.writeString(raw)
//                parcel.writeString(regular)
                parcel.writeString(small)
                parcel.writeString(thumb)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<Urls> {
                override fun createFromParcel(parcel: Parcel): Urls {
                    return Urls(parcel)
                }

                override fun newArray(size: Int): Array<Urls?> {
                    return arrayOfNulls(size)
                }
            }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeString(blurHash)
//            parcel.writeString(color)
//            parcel.writeValue(height)
            parcel.writeString(id)
            parcel.writeParcelable(urls, flags)
//            parcel.writeValue(width)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Result> {
            override fun createFromParcel(parcel: Parcel): Result {
                return Result(parcel)
            }

            override fun newArray(size: Int): Array<Result?> {
                return arrayOfNulls(size)
            }
        }
    }
}
