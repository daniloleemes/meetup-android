package br.com.gdgbrasilia.meetup.app.util.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.view.View
import br.com.gdgbrasilia.meetup.R

fun View.setMarked(liked: Boolean) {
    val colorFrom: Int
    val colorTo: Int
    if (liked) {
        colorFrom = ContextCompat.getColor(context, R.color.colorBlueDark)
        colorTo = ContextCompat.getColor(context, R.color.colorPurple)
    } else {
        colorFrom = ContextCompat.getColor(context, R.color.colorPurple)
        colorTo = ContextCompat.getColor(context, R.color.colorBlueDark)
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.setDuration(250)
            .addUpdateListener { animator -> background.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
    colorAnimation.start()
}

fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}