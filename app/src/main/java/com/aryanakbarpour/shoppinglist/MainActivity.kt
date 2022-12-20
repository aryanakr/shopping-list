package com.aryanakbarpour.shoppinglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.aryanakbarpour.shoppinglist.ui.screens.*
import com.aryanakbarpour.shoppinglist.ui.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel : AuthViewModel by viewModels()

        val skipLoginPref = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getBoolean(
            Constants.SKIP_LOGIN_PREF, false)
        if (authViewModel.isUserAuthenticated || skipLoginPref) {
            navigateToShoppingList()
        }

        setContent {
            ShoppingListTheme() {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
                        composable(Screen.LoginScreen.route) {
                            LoginScreen(navController = navController,viewModel = authViewModel, navigateToShoppingList = {navigateToShoppingList()})
                        }

                        composable(Screen.SignUpScreen.route) {
                            SignUpScreen(navController = navController,viewModel = authViewModel, navigateToShoppingList = {navigateToShoppingList()})
                        }

                    }

                }
            }

        }
    }

    private fun navigateToShoppingList() {
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
    }
}
