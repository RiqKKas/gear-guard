package br.com.gearguard.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "LogCommandEntity")
class LogCommandEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Long = 0L

    @ColumnInfo(name = "commandName")
    var commandName: String = ""

    @ColumnInfo(name = "date")
    var date: LocalDate? = null

    constructor()

    constructor(commandName: String, date: LocalDate){
        this.commandName = commandName
        this.date = date
    }

}