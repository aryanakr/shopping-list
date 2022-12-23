package com.aryanakbarpour.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import com.aryanakbarpour.shoppinglist.model.AppMode
import com.aryanakbarpour.shoppinglist.service.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    val appMode : AppMode
        get() = if (repository.isAuthenticated) AppMode.ONLINE else AppMode.OFFLINE

    suspend fun signOut() = repository.signOut()

    fun getUserName() = repository.displayName
    fun getUserEmail() = repository.photoUrl

}