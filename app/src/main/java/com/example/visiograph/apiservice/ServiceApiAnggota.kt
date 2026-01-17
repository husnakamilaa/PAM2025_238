package com.example.visiograph.apiservice

import com.example.visiograph.modeldata.DataAnggota
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApiAnggota {

    @GET("api/anggota")
    suspend fun getAllAnggota(): List<DataAnggota>
    @GET("api/anggota/{id}")
    suspend fun getAnggotaById(@Path("id") id: Int): DataAnggota
    @GET("api/anggota/search")
    suspend fun searchAnggota(@Query("nama") nama: String): List<DataAnggota>
    @POST("api/anggota")
    suspend fun createAnggota(@Body anggota: DataAnggota): Response<Void>
    @PUT("api/anggota/{id}")
    suspend fun updateAnggota(@Path("id") id: Int, @Body dataAnggota: DataAnggota): Response<Void>
    @DELETE("api/anggota/{id}")
    suspend fun deleteAnggota(@Path("id") id: Int): Response<Void>
}