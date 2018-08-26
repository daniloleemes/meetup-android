package br.com.gdgbrasilia.meetup.app.util.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import br.com.gdgbrasilia.meetup.R

fun FloatingActionButton.setMarked(liked: Boolean) {
    val colorFrom: Int
    val colorTo: Int
    if (liked) {
        colorFrom = ContextCompat.getColor(context, R.color.colorPink)
        colorTo = ContextCompat.getColor(context, R.color.colorPurple)
    } else {
        colorFrom = ContextCompat.getColor(context, R.color.colorPurple)
        colorTo = ContextCompat.getColor(context, R.color.colorPink)
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.setDuration(250)
            .addUpdateListener { animator -> background.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
    colorAnimation.start()
}