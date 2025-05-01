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

    @Query("SELECT * FROM barang ORDER BY nama_barang DESC")
    fun getBarang(): Flow<List<Barang>>

    @Query("SELECT * FROM barang WHERE id = :id")
    suspend fun getBarangById(id: Long): Barang?

    @Query("DELETE FROM barang WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM barang WHERE jumlah = 0")
    suspend fun getOutOfStockCount(): Int

    @Query("SELECT COUNT(*) FROM barang WHERE jumlah < 10 AND jumlah > 0")
    suspend fun getLowStockCount(): Int

    @Query("SELECT SUM(jumlah) FROM barang")
    suspend fun getTotalItemCount(): Int
}