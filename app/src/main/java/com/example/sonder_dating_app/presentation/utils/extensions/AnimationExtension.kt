package com.example.sonder_dating_app.presentation.utils.extensions

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import com.example.sonder_dating_app.R

fun View.startShakeAnimation(){
    this.startAnimationFromResource(R.anim.anim_shake)
}

fun View.startAnimationFromResource(@AnimRes anim: Int){
    val animation = AnimationUtils.loadAnimation(this.context, anim)
    this.startAnimation(animation)
}