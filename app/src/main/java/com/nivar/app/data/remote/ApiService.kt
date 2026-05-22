package com.nivar.app.data.remote

import com.nivar.app.data.model.Grievance
import com.nivar.app.data.model.Scheme
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/v1/schemes/recommend")
    suspend fun getSchemes(): List<Scheme>

    @POST("api/v1/grievances/")
    suspend fun createGrievance(@Body grievance: Grievance): Response<Grievance>
}
