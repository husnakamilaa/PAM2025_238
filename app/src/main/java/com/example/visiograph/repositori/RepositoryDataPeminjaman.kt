package com.example.visiograph.repositori

import com.example.visiograph.apiservice.ServiceApiPeminjaman
import com.example.visiograph.modeldata.DataPeminjaman
import retrofit2.Response

interface RepositoryDataPeminjaman {
    suspend fun getAllPeminjaman(): List<DataPeminjaman>
    suspend fun insertPeminjaman(data: DataPeminjaman): Response<Unit>
    suspend fun updatePeminjaman(id: Int, data: DataPeminjaman): Response<Unit>
    suspend fun deletePeminjaman(id: Int): Response<Unit>
    suspend fun getPeminjamanById(id: Int): DataPeminjaman
}

class JaringanRepositoryPeminjaman(
    private val serviceApiPeminjaman: ServiceApiPeminjaman
) : RepositoryDataPeminjaman {
    override suspend fun getAllPeminjaman() = serviceApiPeminjaman.getAllPeminjaman()
    override suspend fun insertPeminjaman(data: DataPeminjaman) = serviceApiPeminjaman.insertPeminjaman(data)
    override suspend fun updatePeminjaman(id: Int, data: DataPeminjaman) = serviceApiPeminjaman.updatePeminjaman(id, data)
    override suspend fun deletePeminjaman(id: Int) = serviceApiPeminjaman.deletePeminjaman(id)
    override suspend fun getPeminjamanById(id: Int) = serviceApiPeminjaman.getPeminjamanById(id)
}