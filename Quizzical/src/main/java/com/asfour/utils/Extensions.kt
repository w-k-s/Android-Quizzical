package com.asfour.utils

import android.app.Activity
import android.view.View

class Extensions {
}

fun Boolean.asVisibility() : Int{
    return if (this){
        View.VISIBLE
    }else{
        View.GONE
    }
}

val Activity.TAG get() = this.javaClass::getSimpleName