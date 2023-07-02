package com.example.sonder_dating_app.presentation.utils.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.example.sonder_dating_app.R

fun Fragment.navigateUp(){
    this.findNavController().navigateUp()
}

fun Int?.orEmpty(default: Int = 0): Int {
    return this ?: default
}

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}

fun Fragment.navigateTo(@IdRes currentDestination: Int? = null, @IdRes action: Int) {
    if (currentDestination == null) this.findNavController().navigate(action)
    else if (findNavController().currentDestination?.id == R.id.signUpWithPhoneNumber) {
        this.findNavController().navigate(action)
    }
}
