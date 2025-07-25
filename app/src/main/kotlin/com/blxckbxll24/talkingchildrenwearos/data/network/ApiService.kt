package com.blxckbxll24.talkingchildrenwearos.data.network

import com.blxckbxll24.talkingchildrenwearos.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
    
    @GET("child-messages/my-messages")
    suspend fun getMyMessages(@Header("Authorization") authorization: String): Response<MessagesApiResponse>
    
    @GET("child-messages/favorites")
    suspend fun getFavoriteMessages(@Header("Authorization") token: String): Response<MessagesApiResponse>
    
    @PUT("child-messages/{id}")
    suspend fun updateMessage(
        @Path("id") messageId: Int,
        @Header("Authorization") token: String,
        @Body updateData: Map<String, Boolean>
    ): Response<MessagesApiResponse>
    
    @GET("messages/{id}/audio")
    suspend fun getAudioFile(
        @Path("id") messageId: Int,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
}