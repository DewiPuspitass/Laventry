package com.dewipuspitasari0020.laventry.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dewipuspitasari0020.laventry.model.Barang
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {
    @Insert
    suspend fun insert(barang: Barang)

    @Update
    suspend fun update(barang: Barang)

    @Query("SELECT * FROM barang ORDER BY nama_barang Desc")
    fun getBarang(): Flow<List<Barang>>
}