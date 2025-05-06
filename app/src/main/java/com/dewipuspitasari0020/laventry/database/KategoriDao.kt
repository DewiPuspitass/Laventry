package com.dewipuspitasari0020.laventry.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dewipuspitasari0020.laventry.model.Kategori
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Insert
    suspend fun insert(kategori: Kategori)

    @Update
    suspend fun update(kategori: Kategori)

    @Query("SELECT * FROM kategori ORDER BY nama_kategori DESC")
    fun getKategori(): Flow<List<Kategori>>

    @Query("SELECT * FROM kategori WHERE id = :id")
    suspend fun getKategoriById(id: Long): Kategori?

    @Query("DELETE FROM kategori WHERE id = :id")
    suspend fun deleteById(id: Long)
}