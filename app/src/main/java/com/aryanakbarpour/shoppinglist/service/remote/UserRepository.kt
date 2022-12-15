package com.aryanakbarpour.shoppinglist.service.remote

import com.aryanakbarpour.shoppinglist.model.Response


typealias SignOutResponse = Response<Boolean>

interface UserRepository {

    val isAuthenticated: Boolean

    val userId: String

    val displayName: String
    val email: String
    val photoUrl: String

    suspend fun signOut(): SignOutResponse
}