package com.blxckbxll24.talkingchildrenwearos.data.model

// SOLO ESTAS CLASES - eliminar todas las API duplicadas
data class ChildMessage(
    val id: Int = 0,
    val message: MessageDetail,
    val isFavorite: Boolean = false,
    val assignedAt: String? = null,
    val assignedBy: AssignedBy? = null
)

data class MessageDetail(
    val id: Int,
    val text: String,
    val audioUrl: String? = null,
    val category: String = "General", // STRING, no Category
    val categoryColor: String = "#2196F3",
    val categoryIcon: String = "message",
    val isFavorite: Boolean = false,
    val assignedAt: String? = null,
    val assignedBy: String? = null
)

data class Category(
    val id: Int,
    val name: String,
    val color: String = "#2196F3",
    val icon: String = "💬"
)

data class AssignedBy(
    val id: Int,
    val name: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val roleId: Int,
    val roleName: String
)

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: ResponseData?
) {
    // Propiedades computadas para compatibilidad
    val actualToken: String? get() = data?.token
    val actualUser: User? get() = data?.user
}

data class ResponseData(
    val token: String,
    val user: User
)