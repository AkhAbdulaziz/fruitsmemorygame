package uz.gita.fruitsmemorygame.util

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import timber.log.Timber

fun <T : ViewBinding> T.scope(f: T.() -> Unit) {
    f(this)
}

fun timber(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.requireContext(), message, duration).show()
}

fun ViewPropertyAnimator.addEndListener(endBlock: (Boolean) -> Unit): ViewPropertyAnimator {
    var isCancel = false
    this.setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            endBlock(isCancel)
        }

        override fun onAnimationCancel(animation: Animator?) {
            isCancel = true
        }

        override fun onAnimationRepeat(animation: Animator?) {

        }
    })
    return this
}
