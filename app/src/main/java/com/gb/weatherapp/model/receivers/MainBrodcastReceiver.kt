package com.gb.weatherapp.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // будет тост о том что изменилась временная зона. В качестве эксперимента в обучении
        StringBuilder().apply {
            append("ИЗМЕНИЛАСЬ ВРЕМЕННАЯ ЗОНА\n")
            append("Action: ${intent?.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}