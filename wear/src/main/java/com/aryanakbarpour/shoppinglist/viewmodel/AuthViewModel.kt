package com.aryanakbarpour.shoppinglist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.core.service.AuthRepository
import com.aryanakbarpour.shoppinglist.core.model.Response.*
import com.aryanakbarpour.shoppinglist.core.service.OneTapSignInResponse
import com.aryanakbarpour.shoppinglist.core.service.SignInWithGoogleResponse
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    val isUserAuthenticated get() = repo.isUserAuthenticated

    suspend fun loginWithEmail(email: String, password: String) : Response<Boolean> {

        return repo.firebaseLoginWithEmail(email, password)

    }

}