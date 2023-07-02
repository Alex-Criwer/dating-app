package com.example.sonder_dating_app.presentation.utils.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.showSafely(manager: FragmentManager, tag: String? = null): Boolean {
    return if(!manager.isStateSaved) {
        show(manager, tag)
        true
    } else {
        false
    }
}
