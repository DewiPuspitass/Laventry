package com.dewipuspitasari0020.laventry.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "kategori")
data class Kategori(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Long = 0L,
    val nama_kategori: String
)