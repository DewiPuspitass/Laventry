package com.dewipuspitasari0020.laventry.network

import com.dewipuspitasari0020.laventry.model.BarangResponse
import com.dewipuspitasari0020.laventry.model.KategoriResponse
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    suspend fun getKategori(): KategoriResponse

    @POST("kategori/tambah")
    @FormUrlEncoded
    suspend fun insertKategori(
        @Field("nama_kategori") namaKategori: String
    ): ResponseBody

    @PUT("kategori/{id}")
    suspend fun updateKategori(
        @Path("id") id: Long,
        @Body kategori: Map<String, String>
    ): ResponseBody

    @DELETE("kategori/{id}")
    suspend fun deleteKategori(
        @Path("id") id: Long
    ): ResponseBody
}


interface BarangApiService {
    @GET("barang")
    suspend fun getBarang(): BarangResponse

    @Multipart
    @POST("barang/tambah")
    suspend fun insertBarang(
        @Part("nama_barang") namaBarang: RequestBody,
        @Part("jumlah") jumlah: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("kategori_id") kategoriId: RequestBody,
        @Part("barcode") barcode: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part fotoBarang: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("barang/{id}")
    suspend fun getBarangById(
        @Part("id") id: Long
    ): BarangResponse

    @DELETE("barang/{id}")
    suspend fun deleteBarang(
        @Path("id") id: Long
    ): ResponseBody
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