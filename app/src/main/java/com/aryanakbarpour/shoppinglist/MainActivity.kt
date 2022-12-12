package com.aryanakbarpour.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aryanakbarpour.shoppinglist.ui.screens.CreateListScreen
import com.aryanakbarpour.shoppinglist.ui.screens.MainListScreen
import com.aryanakbarpour.shoppinglist.ui.screens.Screen
import com.aryanakbarpour.shoppinglist.ui.screens.ViewListScreen
import com.aryanakbarpour.shoppinglist.ui.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shoppingListViewModel: ShoppingListViewModel by viewModels()

        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.MainListScreen.route) {
                        composable(Screen.MainListScreen.route) {
                            MainListScreen(navController = navController, shoppingListViewModel= shoppingListViewModel)
                        }
                        composable(
                            route = Screen.ViewListScreen.route + "/{listId}",
                            arguments = listOf(navArgument("listId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            })
                        ) { entry ->
                            ViewListScreen(navController = navController, shoppingListViewModel= shoppingListViewModel, listId = entry.arguments?.getLong("listId"))
                        }
                        composable(Screen.CreateListScreen.route) {
                            CreateListScreen(navController = navController, shoppingListViewModel= shoppingListViewModel)
                        }
                        composable(
                            route = Screen.EditListScreen.route + "/{listId}",
                            arguments = listOf(navArgument("listId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            })
                        ) { entry ->
                            CreateListScreen(navController = navController, shoppingListViewModel= shoppingListViewModel, listId = entry.arguments?.getLong("listId"))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShoppingListTheme {
        Greeting("Android")
    }
}