package com.blxckbxll24.talkingchildrenwearos.data.api

import com.blxckbxll24.talkingchildrenwearos.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MessageApiService {
    
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    @GET("messages")
    suspend fun getMyMessages(@Header("Authorization") token: String): Response<MessagesApiResponse>
    
    @GET("messages/favorites")
    suspend fun getFavoriteMessages(@Header("Authorization") token: String): Response<MessagesApiResponse>
    
    @PUT("messages/{id}")
    suspend fun updateMessage(
        @Path("id") messageId: Int,
        @Header("Authorization") token: String,
        @Body updateData: Map<String, Any>
    ): Response<MessagesApiResponse>
    
    @GET("messages/{id}/audio")
    suspend fun getAudioFile(
        @Path("id") messageId: Int,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}