package com.asfour.utils

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

val Any.TAG get() = this.javaClass::getSimpleName