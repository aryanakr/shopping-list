package com.aryanakbarpour.shoppinglist.service.remote

import com.aryanakbarpour.shoppinglist.model.Response


typealias SignOutResponse = Response<Boolean>

interface UserRepository {
    val displayName: String
    val photoUrl: String

    suspend fun signOut(): SignOutResponse
}