package br.com.gearguard.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.gearguard.model.LogCommandEntity

@Dao
interface LogCommandDAO {

    @Insert
    fun save(log: LogCommandEntity): Long

    @Query("Select * From LogCommandEntity")
    fun getAll(): List<LogCommandEntity>

}