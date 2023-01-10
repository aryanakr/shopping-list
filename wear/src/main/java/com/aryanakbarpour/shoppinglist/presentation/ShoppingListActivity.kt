package com.aryanakbarpour.shoppinglist.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aryanakbarpour.shoppinglist.presentation.screens.Screen
import com.aryanakbarpour.shoppinglist.presentation.screens.view_list.ViewListScreen
import com.aryanakbarpour.shoppinglist.presentation.screens.main_list.MainListScreen
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

                NavHost(navController = navController, startDestination = Screen.MainListScreen.route) {

                    composable(Screen.MainListScreen.route) {
                        MainListScreen(navController, userViewModel, shoppingListViewModel) {
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