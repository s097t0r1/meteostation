package com.zmitrovich.meteostation.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.Visibility
import android.view.View

fun View.crossFade(hide: View) {
    val shortAnimationDuration = this.resources.getInteger(android.R.integer.config_shortAnimTime)
    this.apply {
        alpha = 0f
        visibility = View.VISIBLE

        animate()
            .alpha(1f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(null)
    }

    hide.animate()
        .alpha(0f)
        .setDuration(shortAnimationDuration.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                hide.visibility = View.GONE
            }
        })
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}