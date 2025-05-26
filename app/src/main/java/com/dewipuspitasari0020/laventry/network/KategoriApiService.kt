package com.dewipuspitasari0020.laventry.network

import com.dewipuspitasari0020.laventry.model.KategoriRequest
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "https://laventry-api.kakashispiritnews.my.id" + "/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface KategoriApiService {
    @GET("kategori")
    suspend fun getKategori(): String

    @GET("kategori/{id}")
    suspend fun getShowKategori(
        @Path("id") id: Long
    ): String

    @POST("kategori")
    suspend fun createKategori(
        @Body kategori: KategoriRequest
    ): String

    @PUT("kategori/{id}")
    suspend fun updateKategori(
        @Path("id") id: Int,
        @Body kategori: KategoriRequest
    ): String

    @DELETE("kategori/{id}")
    suspend fun deleteKategori(
        @Path("id") id: Int
    ): String
}

object KategoriApi {
    val service: KategoriApiService by lazy {
        retrofit.create(KategoriApiService::class.java)
    }
}