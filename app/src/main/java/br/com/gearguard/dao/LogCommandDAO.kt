package br.com.gearguard.dao

import androidx.room.Dao
import androidx.room.Insert
import br.com.gearguard.model.LogCommandEntity

@Dao
interface LogCommandDAO {

    @Insert
    fun save(log: LogCommandEntity): Long

}