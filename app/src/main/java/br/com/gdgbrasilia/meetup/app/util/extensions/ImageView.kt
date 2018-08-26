package br.com.gdgbrasilia.meetup.app.util.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.data.AppConstants
import br.com.gdgbrasilia.meetup.app.view.components.GlideApp

fun ImageView.setMarked(liked: Boolean, markedColor: Int = R.color.colorPurple) {
    val colorFrom: Int
    val colorTo: Int
    if (liked) {
        colorFrom = ContextCompat.getColor(context, R.color.colorBlueDark)
        colorTo = ContextCompat.getColor(context, markedColor)
    } else {
        colorFrom = ContextCompat.getColor(context, markedColor)
        colorTo = ContextCompat.getColor(context, R.color.colorBlueDark)
    }

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.setDuration(250)
            .addUpdateListener { animator -> drawable.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
    colorAnimation.start()
}

fun ImageView.visibleIfNotNull(subject: Any?) {
    if (subject != null) {
        this.visible()
    }
}

fun ImageView.loadImg(imageUrl: String?) {
    GlideApp.with(context)
            .load(getGlideUrl(imageUrl ?: AppConstants.BASE_URL))
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(this)
}