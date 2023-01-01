package com.aryanakbarpour.shoppinglist.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
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

    Button(onClick = {
        coroutineScope.launch {
            signInResult.value = userViewModel.signOut()
        }
    }) {
        Text(text = "Sign out")
    }
}