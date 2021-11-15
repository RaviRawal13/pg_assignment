package com.ravirawal.pg_assignment.photo_detail.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.ravirawal.pg_assignment.R
import com.ravirawal.pg_assignment.base.BaseFragment
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUsecaseFactory
import com.ravirawal.pg_assignment.databinding.PhotoDetailsFragmentBinding
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.shared_vm.HomeViewModel
import com.ravirawal.pg_assignment.shared_vm.HomeViewModelFactory
import com.ravirawal.pg_assignment.utils.act
import com.ravirawal.pg_assignment.utils.load

class PhotoDetailsFragment :
    BaseFragment<PhotoDetailsFragmentBinding>(R.layout.photo_details_fragment) {

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            SearchPhotosUsecaseFactory.searchPhotosUsecase
        )
    }

    private var currentPhoto: Photo? = null

    private val args: PhotoDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.photo == null) {
            findNavController().popBackStack()
            return
        } else {
            currentPhoto = args.photo
        }
        binding = PhotoDetailsFragmentBinding.bind(view)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        loadImage()

        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.buttonNextPhotoDetail.setOnClickListener {
            val p = viewModel.nextPhoto(currentPhoto)
            if (p != null) {
                currentPhoto = p
                loadImage()
            }
        }
        binding.buttonPreviousPhotoDetail.setOnClickListener {
            val p = viewModel.prevPhoto(currentPhoto)
            if (p != null) {
                currentPhoto = p
                loadImage()
            }
        }
    }


    private fun loadImage() {
        currentPhoto.act {
            binding.photoView.apply {
                transitionName = "shared_element_container ${it.photoId}"
                load(it.full, true)
            }
        }
    }
}
