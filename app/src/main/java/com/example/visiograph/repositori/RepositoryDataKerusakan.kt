package com.example.visiograph.repositori

import com.example.visiograph.apiservice.ServiceApiKerusakan
import com.example.visiograph.modeldata.DataKerusakan
import retrofit2.Response

interface RepositoryDataKerusakan {
    suspend fun getAllKerusakan(): List<DataKerusakan>
    suspend fun createKerusakan(data: DataKerusakan): Response<Unit>
    suspend fun updateKerusakan(id: Int, data: DataKerusakan): Response<Unit>
    suspend fun deleteKerusakan(id: Int): Response<Unit>
    suspend fun getKerusakanById(id: Int): DataKerusakan
}

class JaringanRepositoryKerusakan(
    private val serviceApiKerusakan: ServiceApiKerusakan
) : RepositoryDataKerusakan {
    override suspend fun getAllKerusakan() = serviceApiKerusakan.getAllKerusakan()
    override suspend fun createKerusakan(data: DataKerusakan) = serviceApiKerusakan.createKerusakan(data)
    override suspend fun updateKerusakan(id: Int, data: DataKerusakan) = serviceApiKerusakan.updateKerusakan(id, data)
    override suspend fun deleteKerusakan(id: Int) = serviceApiKerusakan.deleteKerusakan(id)
    override suspend fun getKerusakanById(id: Int) = serviceApiKerusakan.getKerusakanById(id)
}