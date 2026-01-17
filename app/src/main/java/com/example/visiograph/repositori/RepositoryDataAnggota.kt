package com.example.visiograph.repositori

import com.example.visiograph.apiservice.ServiceApiAnggota
import com.example.visiograph.modeldata.DataAnggota
import retrofit2.Response

interface RepositoryDataAnggota {
    suspend fun getAllAnggota(): List<DataAnggota>
    suspend fun getAnggotaById(id: Int): DataAnggota
    suspend fun searchAnggota(nama: String): List<DataAnggota>
    suspend fun createAnggota(dataAnggota: DataAnggota): Response<Void>
    suspend fun updateAnggota(id: Int, dataAnggota: DataAnggota): Response<Void>
    suspend fun deleteAnggota(id: Int): Response<Void>
}

class JaringanRepositoryAnggota(
    private val serviceApiAnggota: ServiceApiAnggota
) : RepositoryDataAnggota {

    override suspend fun getAllAnggota(): List<DataAnggota> =
        serviceApiAnggota.getAllAnggota()

    override suspend fun getAnggotaById(id: Int): DataAnggota =
        serviceApiAnggota.getAnggotaById(id)

    override suspend fun searchAnggota(nama: String): List<DataAnggota> =
        serviceApiAnggota.searchAnggota(nama)

    override suspend fun createAnggota(dataAnggota: DataAnggota): Response<Void> =
        serviceApiAnggota.createAnggota(dataAnggota)

    override suspend fun updateAnggota(id: Int, dataAnggota: DataAnggota): Response<Void> =
        serviceApiAnggota.updateAnggota(id, dataAnggota)

    override suspend fun deleteAnggota(id: Int): Response<Void> =
        serviceApiAnggota.deleteAnggota(id)
}