package com.machiav3lli.derdiedas.utils

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import com.machiav3lli.derdiedas.R
import com.machiav3lli.derdiedas.ui.WordActivity

object AnimationUtil {
    @JvmStatic
    fun animateButtonDrawable(button: AppCompatButton) {
        button.setBackgroundResource(R.drawable.button_animation_correct)
        val buttonAnimation = button.background as AnimationDrawable
        buttonAnimation.start()
    }

    @JvmStatic
    fun animateJumpAndSlide(context: Context, nounView: View, isCorrectAnswer: Boolean) {
        val jumpAnim = AnimationUtils.loadAnimation(context, R.anim.jump_animation)
        val waitAnimation = AnimationUtils.loadAnimation(context, R.anim.wait_animation)
        waitAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                (context as WordActivity).replaceFragment()
            }
        })
        jumpAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                (context as WordActivity).replaceFragment()
            }
        })

        // if correct answer, we do jump and after that slide the new fragment,
        // if wrong, we do flash and after that slide the fragment
        if (isCorrectAnswer) {
            nounView.startAnimation(jumpAnim)
        } else {
            // we are using this dummy animation which does nothing, to wait for the flashing to end
            // and then we replace the fragment (because AnimationDrawable doesn't have a simple
            // onFinished listener) - this means the duration in this animation has to be the sum of
            // durations in button_animation_correct
            nounView.startAnimation(waitAnimation)
        }
    }
}