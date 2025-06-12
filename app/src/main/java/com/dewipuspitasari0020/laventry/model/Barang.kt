package com.dewipuspitasari0020.laventry.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "barang",
    foreignKeys = [
        ForeignKey(
            entity = Kategori::class,
            parentColumns = ["id"],
            childColumns = ["kategoriId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)

data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama_barang: String,
    val jumlah: Int,
    val harga: Double,
    @Json(name = "kategori_id")
    @ColumnInfo(name = "kategoriId")
    val kategoriId: Long,
    val barcode: String,
    val deskripsi: String,
    val foto_barang: String
)

data class BarangResponse(
    val status: Boolean,
    val message: String,
    val data: List<Barang>
)

data class BarangResponseId(
    val status: Boolean,
    val message: String,
    val data: Barang
)

