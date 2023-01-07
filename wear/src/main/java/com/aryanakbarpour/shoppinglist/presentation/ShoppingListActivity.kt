package com.aryanakbarpour.shoppinglist.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material.MaterialTheme
import com.aryanakbarpour.shoppinglist.presentation.screens.AllListsScreen
import com.aryanakbarpour.shoppinglist.presentation.screens.LoginScreen
import com.aryanakbarpour.shoppinglist.presentation.screens.Screen
import com.aryanakbarpour.shoppinglist.presentation.screens.ViewListScreen
import com.aryanakbarpour.shoppinglist.presentation.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val userViewModel : UserViewModel by viewModels()
        val shoppingListViewModel : ShoppingListViewModel by viewModels()

        setContent {
            ShoppingListTheme {

                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = Screen.AllListsScreen.route) {

                    composable(Screen.AllListsScreen.route) {
                        AllListsScreen(navController, userViewModel, shoppingListViewModel) {
                            finish()
                        }

                    }

                    composable(
                        route = Screen.ViewListScreen.route + "/{listId}",
                        arguments = listOf(navArgument("listId") {
                            type = NavType.StringType
                            nullable = false
                        })
                    ) { entry ->
                        ViewListScreen(navController = navController, viewModel= shoppingListViewModel, listId = entry.arguments?.getString("listId")!!)
                    }
                }
            }
        }
    }
}