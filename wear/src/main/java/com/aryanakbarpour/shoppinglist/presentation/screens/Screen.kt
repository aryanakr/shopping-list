package com.aryanakbarpour.shoppinglist.presentation.screens

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object AllListsScreen : Screen("all_lists")
    object ViewListScreen: Screen("view_list")
}