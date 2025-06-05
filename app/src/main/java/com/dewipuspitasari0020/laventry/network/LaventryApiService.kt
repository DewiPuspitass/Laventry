package com.dewipuspitasari0020.laventry.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://laventry-api.kakashispiritnews.my.id" + "/api/"
private const val BASE_IMAGE_URL = "https://laventry-api.kakashispiritnews.my.id/images/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface KategoriApiService {
    @GET("kategori")
    suspend fun getKategori(): String
}


interface BarangApiService {
    @GET("barang")
    suspend fun getBarang(): String
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