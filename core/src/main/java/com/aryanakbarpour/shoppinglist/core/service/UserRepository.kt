package com.aryanakbarpour.shoppinglist.core.service

import com.aryanakbarpour.shoppinglist.core.model.Response


typealias SignOutResponse = Response<Boolean>

interface UserRepository {

    val isAuthenticated: Boolean

    val userId: String

    val displayName: String
    val email: String
    val photoUrl: String

    suspend fun signOut(): SignOutResponse
}