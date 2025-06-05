package com.dewipuspitasari0020.laventry.network

import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.BarangResponse
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.model.KategoriResponse
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://laventry-api.kakashispiritnews.my.id" + "/api/"
private const val BASE_IMAGE_URL = "https://laventry-api.kakashispiritnews.my.id/images/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KategoriApiService {
    @GET("kategori")
    suspend fun getKategori(): KategoriResponse
}


interface BarangApiService {
    @GET("barang")
    suspend fun getBarang(): BarangResponse
}

object KategoriApi {
    val service: KategoriApiService by lazy {
        retrofit.create(KategoriApiService::class.java)
    }
}

object BarangApi {
    val service: BarangApiService by lazy {
        retrofit.create(BarangApiService::class.java)
    }
}