package br.com.gearguard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.gearguard.model.LogCommandEntity

@Database(entities = [LogCommandEntity::class], version = 1, exportSchema = false)
abstract class SQLiteROOM(): RoomDatabase() {
    companion object{
        private lateinit var INSTANCE: SQLiteROOM

        fun getBancoROOM(contexto:Context): SQLiteROOM {
            if(!::INSTANCE.isInitialized){
                synchronized(SQLiteROOM::class) {
                    INSTANCE = Room.databaseBuilder(contexto, SQLiteROOM::class.java,
                        "gear_guard").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }

}
