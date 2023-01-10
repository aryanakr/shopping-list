package com.aryanakbarpour.shoppinglist.core.service

import com.aryanakbarpour.shoppinglist.core.model.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>

interface AuthRepository {

    val isUserAuthenticated: Boolean

    /**
     * This method is used to sign in with google
     * @return OneTapSignInResponse
     */
    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

    /**
     * This method is used to sign in with google and handle new user creation
     * @param googleCredential AuthCredential
     */
    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse

    /**
     * This method is used to register using email and password
     * @param email String
     * @param password String
     * @param name String
     * @return Response<Boolean> true if user is registered successfully
     */
    suspend fun firebaseSignUpWithEmail(email: String, password: String, name: String) : Response<Boolean>

    /**
     * This method is used to sign in using email and password
     * @param email String
     * @param password String
     * @return Response<Boolean> true if user is signed in successfully
     */
    suspend fun firebaseLoginWithEmail(email: String, password: String) : Response<Boolean>

    /**
     * This method checks if the given email is already registered
     * @param email String
     * @return Response<Boolean> true if email is already registered
     */
    suspend fun checkIfUserExists(email: String) : Response<Boolean>
}