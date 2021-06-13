package com.grank.datacenter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grank.datacenter.db.DemoDao
import com.grank.datacenter.db.DemoEntity

@Database(entities = [DemoEntity::class], version = 3, exportSchema = true)
abstract class MainDb : RoomDatabase() {
    companion object {
        private const val DB_NAME = "main_data.db"
        fun create(context: Context): MainDb {
            return Room.databaseBuilder(
                context, MainDb::class.java,
                DB_NAME)
                .addMigrations(migration_1_2, migration_2_3)
                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationOnDowngrade()
                .createFromAsset("file.db")  //会从asserts目录下读取并预置数据
                .build()
        }
    }

    abstract fun getDemoDao(): DemoDao
}

//在表中添加列
private val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE demoEntity ADD COLUMN sex INTEGER NOT NULL DEFAULT 1")
    }
}

//升级保留数据 升级数据库，将列sex的类型弄换成TEXT
//step: 新建一个表，将旧表数据COPY到新表，删除旧表，新新表改名为刚才的表
//`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `sex` INTEGER NOT NULL
private val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE tmpDemoEntity (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "name TEXT," +
                    "sex TEXT DEFAULT 'm'" +
                    ")"
        )
        database.execSQL("INSERT INTO tmpDemoEntity (name, sex)" +
                    "SELECT name, sex FROM demoEntity"
        )
        database.execSQL("DROP TABLE demoEntity")
        database.execSQL("ALTER TABLE tmpDemoEntity RENAME TO demoEntity")
    }
}