package com.scb.omdb.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

fun isNetworkAvailable(activity: Activity): Boolean {
    val connectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun convertDate(date: Date): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return format.format(date)
}

fun setPosterImage(context: Context, url: String?, imageView: ImageView) {
    if (url.isNullOrBlank()) return

    val requestOptions = RequestOptions().diskCacheStrategy(
        DiskCacheStrategy.ALL
    )
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(url)
        .into(imageView)
}

fun showAlertDialog(context: Context, title: Int, message: Int) {
    val dialog = AlertDialog.Builder(context)
    dialog.setTitle(title)
    dialog.setMessage(message)
    dialog.setPositiveButton(android.R.string.ok, null)
    dialog.show()
}