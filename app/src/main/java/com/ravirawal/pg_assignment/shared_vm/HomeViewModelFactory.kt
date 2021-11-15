package com.ravirawal.pg_assignment.shared_vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUseCase

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(searchPhotosUseCase) as T
    }
}