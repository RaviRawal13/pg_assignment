package com.ravirawal.pg_assignment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ravirawal.pg_assignment.data.usecases.SearchPhotosUsecaseFactory
import com.ravirawal.pg_assignment.databinding.ActivityMainBinding
import com.ravirawal.pg_assignment.shared_vm.HomeViewModel
import com.ravirawal.pg_assignment.shared_vm.HomeViewModelFactory
import com.ravirawal.pg_assignment.utils.COLUMN_COUNT_FOUR
import com.ravirawal.pg_assignment.utils.COLUMN_COUNT_THREE
import com.ravirawal.pg_assignment.utils.COLUMN_COUNT_TWO
import com.ravirawal.pg_assignment.utils.showToast

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            SearchPhotosUsecaseFactory.searchPhotosUsecase
        )
    }

    private val singleItems: Array<String> by lazy {
        resources.getStringArray(R.array.column_options)
    }

    private var showColumnsOption = true
    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showColumnsOption = true
                    invalidateOptionsMenu()
                }
                R.id.photoDetailsFragment -> {
                    showColumnsOption = false
                    invalidateOptionsMenu()
                }
            }
        }

    private var columnCount = COLUMN_COUNT_TWO

    private var dialogSelectedIndex = 0

    private fun columnChangeDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.menu_filter))
            .setSingleChoiceItems(singleItems, dialogSelectedIndex)
            { dialog, which ->
                columnCount = when (which) {
                    1 -> COLUMN_COUNT_THREE
                    2 -> COLUMN_COUNT_FOUR
                    else -> {
                        COLUMN_COUNT_TWO
                    }
                }
                dialogSelectedIndex = which
                showToast("Showing ${singleItems[which]} per column")
                onColumnChange(columnCount)

                dialog.dismiss()
            }
            .show()
    }

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navController)
    }

    override fun onResume() {
        navController.addOnDestinationChangedListener(listener)
        super.onResume()
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (showColumnsOption) {
            val inflater = menuInflater
            inflater.inflate(R.menu.top_app_bar, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.filter) {
            columnChangeDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()

    private fun onColumnChange(columnCount: Int) {
        viewModel.columnCount.value = columnCount
    }
}
