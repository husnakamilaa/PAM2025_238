package com.example.visiograph.apiservice

import com.example.visiograph.modeldata.login.LoginRequest
import com.example.visiograph.modeldata.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceApiAuth {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}