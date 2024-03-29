package com.aryanakbarpour.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aryanakbarpour.shoppinglist.core.model.AppMode
import com.aryanakbarpour.shoppinglist.ui.screens.*
import com.aryanakbarpour.shoppinglist.ui.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.core.Constants
import com.aryanakbarpour.shoppinglist.ui.screens.create_list.CreateListScreen
import com.aryanakbarpour.shoppinglist.ui.screens.main_list.MainListScreen
import com.aryanakbarpour.shoppinglist.ui.screens.view_list.ViewListScreen
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shoppingListViewModel: ShoppingListViewModel by viewModels()
        val userViewModel: UserViewModel by viewModels()

        // set skip offline skip login preference
        if (userViewModel.appMode == AppMode.OFFLINE) {
            getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit().putBoolean(
                Constants.SKIP_LOGIN_PREF, true).apply()
        } else {
            getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit().putBoolean(
                Constants.SKIP_LOGIN_PREF, false).apply()
        }

        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.MainListScreen.route) {
                        composable(Screen.MainListScreen.route) {
                            MainListScreen(
                                navController = navController,
                                shoppingListViewModel= shoppingListViewModel,
                                userViewModel = userViewModel,
                                navigateToLogin = {
                                    getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                                        .edit().putBoolean(Constants.SKIP_LOGIN_PREF, false).apply()
                                    finish()
                                }
                            )
                        }
                        composable(
                            route = Screen.ViewListScreen.route + "/{listId}",
                            arguments = listOf(navArgument("listId") {
                                type = NavType.StringType
                                nullable = false
                            })
                        ) { entry ->
                            ViewListScreen(navController = navController,
                                shoppingListViewModel= shoppingListViewModel,
                                listId = entry.arguments?.getString("listId")!!
                            )
                        }
                        composable(Screen.CreateListScreen.route) {
                            CreateListScreen(navController = navController,
                                shoppingListViewModel= shoppingListViewModel)
                        }
                        composable(
                            route = Screen.EditListScreen.route + "/{listId}",
                            arguments = listOf(navArgument("listId") {
                                type = NavType.StringType
                                defaultValue = null
                                nullable = true
                            })
                        ) { entry ->
                            CreateListScreen(navController = navController,
                                shoppingListViewModel= shoppingListViewModel,
                                listId = entry.arguments?.getString("listId"))
                        }
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.route == Screen.MainListScreen.route) {
            return
        } else {
            super.onBackPressed()
        }
    }
}