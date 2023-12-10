package br.com.gearguard.controller

import android.content.Context
import br.com.gearguard.dao.LogCommandDAO
import br.com.gearguard.database.SQLiteROOM
import br.com.gearguard.model.LogCommandEntity

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class LogCommandController(var context: Context) {

    lateinit var dao: LogCommandDAO

    init {
        dao = SQLiteROOM.getBancoROOM(context).logCommandDAO()
    }

    fun insert(commandName: String): Boolean {
        if (commandName != "") {
            val timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
            val calendar = Calendar.getInstance(timeZone)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = timeZone
            val dateInBrazil = dateFormat.format(calendar.time)

            val command = LogCommandEntity(commandName, dateInBrazil)
            val save =  dao.save(command) >= 1

            if (save) {
                this.executeCommand(commandName)
                return true
            }
        }

        return false
    }

    fun getAllLogs(): MutableList <LogCommandEntity> {
        return dao.getAll().toMutableList()
    }

    private fun executeCommand(commandName: String): Boolean {
        return true
    }

}