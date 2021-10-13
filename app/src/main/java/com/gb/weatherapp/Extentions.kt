package com.gb.weatherapp.framework

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    //Extention функция  и одноврменно функция высшего порядка, т.к.  один из аргументов - функция
    text: String,
    actionText: String,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    action: (View) -> Unit,
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}