package com.example.base.kxt

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout

/**
 * @author:SunShibo
 * @date:2022-09-07 15:03
 * @feature: Some View ext
 */

fun ProgressBar.show() {
    visibility = View.VISIBLE
}


fun ProgressBar.hide() {
    visibility = View.GONE
}

fun TabLayout.Tab.hideLongClickable(){
    this.view.isLongClickable = false
    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
        this.view.tooltipText = ""
    }
}


fun ImageView.loadImage(imageUrl: String){
    Glide.with(this).load(imageUrl).into(this)
}