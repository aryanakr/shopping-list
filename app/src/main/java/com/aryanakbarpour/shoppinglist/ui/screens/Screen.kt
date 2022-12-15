package com.aryanakbarpour.shoppinglist.ui.screens

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object MainListScreen: Screen("main_list_screen")
    object CreateListScreen: Screen("create_list_screen")
    object EditListScreen: Screen("edit_list_screen")
    object ViewListScreen: Screen("view_list_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/${arg}")
            }
        }
    }

}
