package com.ravirawal.pg_assignment.home.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ravirawal.pg_assignment.R
import com.ravirawal.pg_assignment.base.BaseFragment
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUsecaseFactory
import com.ravirawal.pg_assignment.databinding.HomeFragmentBinding
import com.ravirawal.pg_assignment.home.adapter.HomeAdapter
import com.ravirawal.pg_assignment.home.adapter.LoadingStateAdapter
import com.ravirawal.pg_assignment.model_ui.Photo
import com.ravirawal.pg_assignment.shared_vm.HomeViewModel
import com.ravirawal.pg_assignment.shared_vm.HomeViewModelFactory
import com.ravirawal.pg_assignment.utils.COLUMN_COUNT_TWO
import com.ravirawal.pg_assignment.utils.MIN_SEARCH_CHAR_LEN
import com.ravirawal.pg_assignment.utils.act
import com.ravirawal.pg_assignment.utils.dismissKeyboard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * Shared element transition
 * database
 * test cases
 * retain data on rotate no api call
 *
 * */

class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            SearchPhotosUsecaseFactory.searchPhotosUsecase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter { photo: Photo, view: View, _ ->
            viewModel.photoList = homeAdapter.snapshot().items

            val extras = FragmentNavigator.Extras.Builder()
                .addSharedElements(
                    mapOf(view to view.transitionName)
                ).build()

            val action = HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(photo)
            findNavController().navigate(action, extras)
        }.apply {
            stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            addLoadStateListener { loadState ->
                binding.apply {
                    progressbarPhotosMain.isVisible = loadState.source.refresh is LoadState.Loading
                    recyclerViewPhotosMain.isVisible =
                        loadState.source.refresh is LoadState.NotLoading
                    buttonRetryMain.isVisible = loadState.source.refresh is LoadState.Error
                    textViewNoInternetConnectionMain.isVisible =
                        loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && homeAdapter.itemCount < 1
                    ) {
                        recyclerViewPhotosMain.isVisible = false
                        textViewNoResultMain.isVisible = true
                    } else {
                        textViewNoResultMain.isVisible = false
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
        setupViews()
        initObservations()
    }

    private fun setupViews() {
        with(binding) {

            recyclerViewPhotosMain.adapter = homeAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { homeAdapter.retry() },
                footer = LoadingStateAdapter { homeAdapter.retry() }
            )

            postponeEnterTransition()

            recyclerViewPhotosMain.viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }

            searchViewPhotosMain.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchViewPhotosMain.dismissKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.length > MIN_SEARCH_CHAR_LEN)
                        performSearch(newText)

                    return false
                }

            })
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchPhotos(query)
    }

    private fun initObservations() {
        lifecycleScope.launch {
            viewModel.searchResult.collectLatest { pagedData ->
                homeAdapter.submitData(pagedData)
            }
        }

        viewModel.columnCount.observe(viewLifecycleOwner) { colCount ->
            (binding.recyclerViewPhotosMain.layoutManager as? GridLayoutManager).act {
                it.spanCount = colCount ?: COLUMN_COUNT_TWO
            }
        }
    }
}
