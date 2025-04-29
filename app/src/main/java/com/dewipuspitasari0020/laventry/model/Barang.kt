package com.dewipuspitasari0020.laventry.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang")
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val nama_barang: String,
    val jumlah:Int,
    val harga:Double,
    val kategori:String,
    val barcode:String,
    val Description:String,
    val foto_barang:String
)