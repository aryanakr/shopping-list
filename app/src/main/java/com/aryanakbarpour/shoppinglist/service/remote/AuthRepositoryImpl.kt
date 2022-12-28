package com.aryanakbarpour.shoppinglist.service.remote

import android.util.Log
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.core.model.Response.Success
import com.aryanakbarpour.shoppinglist.core.model.Response.Failure
import com.aryanakbarpour.shoppinglist.core.model.User
import com.aryanakbarpour.shoppinglist.core.service.AuthRepository
import com.aryanakbarpour.shoppinglist.core.service.OneTapSignInResponse
import com.aryanakbarpour.shoppinglist.core.service.SignInWithGoogleResponse
import com.aryanakbarpour.shoppinglist.core.Constants.SIGN_IN_REQUEST
import com.aryanakbarpour.shoppinglist.core.Constants.SIGN_UP_REQUEST
import com.aryanakbarpour.shoppinglist.core.Constants.USERS
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore
) : AuthRepository {

    override val isUserAuthenticated: Boolean
        get() = auth.currentUser != null

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpResult)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                print("kir")
                addUserToFirestore()
            }
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    override suspend fun firebaseSignUpWithEmail(email: String, password: String, name: String) : Response<Boolean> {

        if (email.trim().isBlank() || password.trim().isBlank()) {
            return Failure(Exception("Please fill all fields"))
        }

        if (password.length < 8) {
            return Failure(Exception("Password must be at least 8 characters"))
        }

        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore(name)
                Success(true)
            }
            else {
                Log.d("pest", "firebaseSignUpWithEmail: user already exists")
                Failure(Exception("User already exists"))
            }

        } catch (e: Exception) {
            Log.d("pest", "signup failure: ${e.message},,, ${e}")
            Failure(e)
        }
    }

    override suspend fun firebaseLoginWithEmail(email: String, password: String) : Response<Boolean> {

        if (email.trim().isBlank()) {
            return Failure(Exception("Email is empty"))
        }

        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            Success(true)
        } catch (e: Exception) {
            Log.d("pest", e.toString())
            Failure(e)
        }
    }

    override suspend fun checkIfUserExists(email: String): Response<Boolean> {
        return try {
            val querySnapshot = db.collection(USERS).whereEqualTo("email", email).get().await()
            Success(querySnapshot.documents.isNotEmpty())
        } catch (e: Exception) {
            Failure(e)
        }
    }

    private suspend fun addUserToFirestore(name: String? = null) {
        auth.currentUser?.apply {
            val user = User(
                id = uid,
                name = name ?: displayName,
                email = email
            )
            db.collection(USERS).document(uid).set(user).await()
        }
    }


}
