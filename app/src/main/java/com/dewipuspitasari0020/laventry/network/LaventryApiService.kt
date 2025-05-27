package com.dewipuspitasari0020.laventry.network

import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.BarangResponse
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.model.KategoriResponse
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
    suspend fun getKategori(): List<Kategori>

    @GET("kategori/{id}")
    suspend fun getShowKategori(
        @Path("id") id: Long
    ): String

    @POST("kategori")
    suspend fun createKategori(
        @Body kategori: KategoriResponse
    ): String

    @PUT("kategori/{id}")
    suspend fun updateKategori(
        @Path("id") id: Int,
        @Body kategori: KategoriResponse
    ): String

    @DELETE("kategori/{id}")
    suspend fun deleteKategori(
        @Path("id") id: Int
    ): String
}

interface BarangApiService {
    @GET("barang")
    suspend fun getBarang(): BarangResponse

    @GET("barang/{id}")
    suspend fun getShowBarang(
        @Path("id") id: Long
    ): String

    @POST("barang")
    suspend fun createBarang(
        @Body barang: BarangResponse
    ): String

    @PUT("barang/{id}")
    suspend fun updateBarang(
        @Path("id") id: Int,
        @Body barang: BarangResponse
    ): String

    @DELETE("barang/{id}")
    suspend fun deleteBarang(
        @Path("id") id: Int
    ): String
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

    fun getGambarUrl(foto_barang: String): String {
        return "$BASE_IMAGE_URL$foto_barang"
    }
}
