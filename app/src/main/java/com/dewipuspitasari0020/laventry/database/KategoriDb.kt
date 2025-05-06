package com.dewipuspitasari0020.laventry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dewipuspitasari0020.laventry.model.Kategori

@Database(entities = [Kategori::class], version = 1, exportSchema = false)
abstract class KategoriDb:RoomDatabase() {
    abstract val dao: KategoriDao

    companion object {
        @Volatile
        private var INSTANCE: KategoriDb? = null

        fun getInstance(context: Context): KategoriDb{
            synchronized(this){
                var instance = KategoriDb.INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KategoriDb::class.java,
                        "kategori.db"
                    ).build()
                    KategoriDb.INSTANCE = instance
                }
                return instance
            }
        }
    }
}