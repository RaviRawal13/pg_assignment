package com.ravirawal.pg_assignment.data.usecases

import com.ravirawal.pg_assignment.data.repository.UnsplashPhotoRepositoryFactory

object SearchPhotosUsecaseFactory {
    val searchPhotosUsecase by lazy { SearchPhotosUseCase(UnsplashPhotoRepositoryFactory.getUnsplashPhotoRepository()) }
}