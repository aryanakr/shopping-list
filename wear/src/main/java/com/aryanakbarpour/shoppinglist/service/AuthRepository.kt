package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>

interface AuthRepository {

    val isUserAuthenticated: Boolean

    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse

    suspend fun firebaseLoginWithEmail(email: String, password: String) : Response<Boolean>

    suspend fun checkIfUserExists(email: String) : Response<Boolean>
}