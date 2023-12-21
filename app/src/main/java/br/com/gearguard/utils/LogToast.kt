package br.com.gearguard.utils

import android.content.Context
import android.widget.Toast

class LogToast {
    companion object {
        fun logToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}