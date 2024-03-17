package com.example.vkandroidpasswordmanager.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.vkandroidpasswordmanager.R

object ViewExtensions {

    fun ImageView.loadFavicon(url: String) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.baseline_image_24)
            .into(this)
    }
}