package com.gb.weatherapp.framework

import android.content.Context
import android.view.View
import android.widget.Toast
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
//Extention функция для тоста
fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()