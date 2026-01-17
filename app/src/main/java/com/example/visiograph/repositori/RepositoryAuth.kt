package com.example.visiograph.repositori

import com.example.visiograph.apiservice.ServiceApiAuth
import com.example.visiograph.modeldata.LoginRequest
import com.example.visiograph.modeldata.LoginResponse
import retrofit2.Response

interface RepositoryAuth {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
}

class JaringanAuthRepository(
    private val serviceApiAuth: ServiceApiAuth
) : RepositoryAuth {
    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        serviceApiAuth.login(request)
}