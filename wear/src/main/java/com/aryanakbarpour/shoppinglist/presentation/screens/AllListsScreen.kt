package com.aryanakbarpour.shoppinglist.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun AllListsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    shoppingListViewModel: ShoppingListViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val signOutResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signOutResult.value is Response.Success) {
        signOutResult.value = null
        navController.navigate(Screen.LoginScreen.route)
    }

    val shoppingListsState = shoppingListViewModel.shoppingListsFlow.collectAsState(initial = listOf())

    Box(modifier = Modifier
        .fillMaxSize().background(PrimaryDark),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier
                .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(shoppingListsState.value.size) { index ->
                    val shoppingList = shoppingListsState.value[index].shoppingList
                    DashboardListItem(shoppingList = shoppingList) {
                        navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.id))
                    }
                }

                item {
                    Button(onClick = {
                        Log.d("sign out", "sign out")
                        coroutineScope.launch {
                            signOutResult.value = userViewModel.signOut()
                        }
                    }) {
                        Text(text = "Sign out")
                    }
                }
            }

        }

    }
}

@Composable
fun DashboardListItem(shoppingList: ShoppingList, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(40.dp)
        .background(Primary)
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = shoppingList.name)
    }
}