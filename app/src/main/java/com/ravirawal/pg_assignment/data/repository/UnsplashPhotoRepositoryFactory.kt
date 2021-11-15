package com.ravirawal.pg_assignment.data.repository

import com.ravirawal.pg_assignment.data.remote.ServiceHelper

object UnsplashPhotoRepositoryFactory {
    fun getUnsplashPhotoRepository() = UnsplashPhotoRepository.UnsplashPhotoRepositoryImpl(ServiceHelper.getApiService())
}