package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.Response.*
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore
) : UserRepository {

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null

    override val userId: String = auth.currentUser?.uid ?: ""

    override val displayName = auth.currentUser?.displayName ?: "Offline User"
    override val photoUrl = (auth.currentUser?.photoUrl ?: "").toString()
    override val email = auth.currentUser?.email ?: ""

    override suspend fun signOut(): SignOutResponse {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

}