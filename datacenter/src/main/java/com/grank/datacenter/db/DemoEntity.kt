package com.grank.datacenter.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DemoEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    @ColumnInfo(name="name",typeAffinity = ColumnInfo.TEXT,defaultValue = "n")
    val name:String,
    val sex:String
)
