package uz.gita.fruitsmemorygame.util

import android.view.View
import android.widget.ImageView
import uz.gita.fruitsmemorygame.R
import uz.gita.fruitsmemorygame.data.ImageData


fun ImageView.closeAnimation(block: () -> Unit) {
    this.animate().setDuration(250).rotationY(91f).withEndAction {
        rotationY = 89f
        this.setImageResource(R.drawable.image_back)
        this.animate().setDuration(250).rotationY(0f).withEndAction {
            block()
        }.start()
    }.start()
}

fun ImageView.closeAnimation() {
    this.animate().setDuration(250).rotationY(91f).withEndAction {
        rotationY = 89f
        this.setImageResource(R.drawable.image_back)
        this.animate().setDuration(250).rotationY(0f).start()
    }.start()
}

fun ImageView.openAnimation(block: () -> Unit) {
    this.animate().setDuration(250).rotationY(89f).withEndAction {
        rotationY = 91f
        this.setImageResource((this.tag as ImageData).imageURL)
        this.scaleType = ImageView.ScaleType.FIT_CENTER
        this.animate().setDuration(250).rotationY(180f).withEndAction {
            block()
        }.start()
    }.start()
}


fun ImageView.openAnimation() {
    this.animate().setDuration(250).rotationY(89f).withEndAction {
        rotationY = 91f
        this.setImageResource((this.tag as ImageData).imageURL)
        this.scaleType = ImageView.ScaleType.FIT_CENTER
        this.animate().setDuration(250).rotationY(180f).start()
    }.start()
}


fun ImageView.remove(block: ImageView.() -> Unit) {
    this.animate().setDuration(250).scaleY(0f).scaleX(0f).withEndAction {
        this.gone()
        block(this)
    }.start()
}

fun ImageView.hideImage(block: ImageView.() -> Unit) {
    this.animate().setDuration(500L)
        .scaleY(0f)
        .scaleX(0f)
        .alpha(0f)
        .withEndAction {
            this.gone()
            block(this)
        }.start()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}