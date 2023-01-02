package com.aryanakbarpour.shoppinglist.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun AllListsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    shoppingListViewModel: ShoppingListViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signInResult.value is Response.Success) {
        signInResult.value = null
        navController.navigate(Screen.LoginScreen.route)
    }

    if (signInResult.value is Response.Failure) {
        Log.d("signin", (signInResult.value as Response.Failure).e.toString())
    }
    Scaffold() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = {
                Log.d("sign out", "sign out")
                coroutineScope.launch {
                    signInResult.value = userViewModel.signOut()
                }
            }) {
                Text(text = "Sign out")
            }
        }

    }

}