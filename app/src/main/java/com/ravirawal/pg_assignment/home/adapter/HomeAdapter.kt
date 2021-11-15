package com.ravirawal.pg_assignment.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ravirawal.pg_assignment.databinding.PhotoItemLayoutBinding
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.model_ui.PhotoDiffCallBack
import com.ravirawal.pg_assignment.utils.act
import com.ravirawal.pg_assignment.utils.load

class HomeAdapter(private val onPhotoSelected: (photo: Photo, view: View, position: Int) -> Unit = { _, _, _ -> run {} }) :
    PagingDataAdapter<Photo, HomeAdapter.PhotoViewHolder>(PhotoDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotoViewHolder(
            PhotoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), position, onPhotoSelected)
    }

    class PhotoViewHolder(private val itemBinding: PhotoItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(p: Photo?, position: Int, onPhotoSelected: (Photo, View, Int) -> Unit) {
            p.act { photo ->
                itemBinding.photoView.apply {
                    transitionName = "shared_element_container ${photo.photoId}"
                    load(photo.full)

                    setOnClickListener {
                        onPhotoSelected(photo, this, position)
                    }
                }
            }
        }
    }
}
