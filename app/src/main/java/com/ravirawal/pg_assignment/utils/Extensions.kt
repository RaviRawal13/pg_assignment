package com.ravirawal.pg_assignment.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.ravirawal.pg_assignment.R
import kotlinx.coroutines.*

fun View.showSnack(
    message: String,
    action: String = "",
    actionListener: () -> Unit = {}
): Snackbar {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    if (action != "") {
        snackbar.duration = Snackbar.LENGTH_INDEFINITE
        snackbar.setAction(action) {
            actionListener()
            snackbar.dismiss()
        }
    }
    snackbar.show()
    return snackbar
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun SearchView.dismissKeyboard() {
    val imm: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun ImageView.load(url: String?, forceRetrieveFromCache: Boolean = false) {
    Glide.with(this)
        .load(url ?: "")
        .onlyRetrieveFromCache(forceRetrieveFromCache)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .fitCenter()
        .dontAnimate()
        .dontTransform()
        .placeholder(R.drawable.ic_image_background)
        .error(R.drawable.ic_image_background)
        .into(this)
}

fun ImageView.load(@DrawableRes drawableRes: Int) {
    Glide.with(this).load(drawableRes).fitCenter().into(this)
}

fun String?.default(default: String? = null): String {
    if (this.isNullOrEmpty()) {
        return default ?: ""
    }
    return this
}

inline fun <T : Any> T?.act(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

fun View.delayOnLifeCycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycleScope.launch(dispatcher) {
        delay(durationInMillis)
        block.invoke()
    }
}

