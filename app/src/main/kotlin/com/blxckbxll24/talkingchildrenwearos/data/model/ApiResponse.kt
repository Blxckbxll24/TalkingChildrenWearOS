package com.blxckbxll24.talkingchildrenwearos.data.model

data class MessagesApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<ChildMessageApiResponse>? = null
)

data class ChildMessageApiResponse(
    val id: Int,
    val message: MessageApiResponse,
    val is_favorite: Int, // INT en lugar de Boolean
    val assigned_at: String,
    val assigned_by: AssignedByApiResponse
)

data class MessageApiResponse(
    val id: Int,
    val text: String,
    val audio_url: String?,
    val category: CategoryApiResponse
)

data class CategoryApiResponse(
    val id: Int,
    val name: String,
    val color: String,
    val icon: String
)

data class AssignedByApiResponse(
    val id: Int,
    val name: String
)

data class ApiMessage(
    val id: Int,
    val text: String,
    val audio_url: String,
    val category_id: Int,
    val created_by: Int,
    val is_active: Int,
    val created_at: String,
    val updated_at: String,
    val category_name: String?,
    val creator_name: String?
)