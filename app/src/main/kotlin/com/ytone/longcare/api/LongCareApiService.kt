package com.ytone.longcare.api

import com.ytone.longcare.model.Response
import retrofit2.http.POST

interface LongCareApiService {

    @POST("V1/Login/Phone")
    suspend fun phoneLogin(): Response<String>
}