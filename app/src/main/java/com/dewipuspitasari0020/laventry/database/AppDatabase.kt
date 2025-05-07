package com.dewipuspitasari0020.laventry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.Kategori

@Database(entities = [Barang::class, Kategori::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract val barangDao: BarangDao
    abstract val kategoriDao: KategoriDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "barang.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}