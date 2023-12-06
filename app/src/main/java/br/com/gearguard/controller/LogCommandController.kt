package br.com.gearguard.controller

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import br.com.gearguard.dao.LogCommandDAO
import br.com.gearguard.database.SQLiteROOM
import br.com.gearguard.model.LogCommandEntity
import java.time.LocalDateTime

class LogCommandController(var contexto:Context) {

    lateinit var dao: LogCommandDAO

    init {
        dao = SQLiteROOM.getBancoROOM(contexto).logCommandDAO()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(commandName: String): Boolean {
        if (commandName != "") {
            val data = LocalDateTime.now()
            val command: LogCommandEntity = LogCommandEntity(commandName, data)

            return dao.save(command) >= 1
        }

        return false
    }

}