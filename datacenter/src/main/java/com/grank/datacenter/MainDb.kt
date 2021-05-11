package com.grank.datacenter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grank.datacenter.db.DemoDao
import com.grank.datacenter.db.DemoEntity

@Database(entities = [DemoEntity::class], version = 1, exportSchema = false)
abstract class MainDb : RoomDatabase() {
    companion object {
        private const val DB_NAME = "main_data.db"
        fun create(context: Context): MainDb {
            return Room.databaseBuilder(
                context, MainDb::class.java,
                DB_NAME
            ).build()
        }
    }
    abstract fun getDemoDao(): DemoDao
}