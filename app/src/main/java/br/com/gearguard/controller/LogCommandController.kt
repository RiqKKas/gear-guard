package br.com.gearguard.controller

import android.content.Context
import br.com.gearguard.dao.LogCommandDAO
import br.com.gearguard.database.SQLiteROOM
import br.com.gearguard.model.LogCommandEntity

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class LogCommandController(var context: Context) {

    lateinit var dao: LogCommandDAO

    init {
        dao = SQLiteROOM.getBancoROOM(context).logCommandDAO()
    }

    fun insert(commandName: String): Boolean {
        if (commandName != "") {
            val calendar = Calendar.getInstance()
            val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(calendar.time)
            val command = LogCommandEntity(commandName, date)

            return dao.save(command) >= 1
        }

        return false
    }

    fun getAllLogs(): MutableList <LogCommandEntity> {
        return dao.getAll().toMutableList()
    }

}