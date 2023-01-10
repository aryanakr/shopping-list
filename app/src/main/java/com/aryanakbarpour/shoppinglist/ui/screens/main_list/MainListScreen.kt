package com.aryanakbarpour.shoppinglist.ui.screens.main_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.core.model.AppMode
import com.aryanakbarpour.shoppinglist.ui.components.AnimatedSurface
import com.aryanakbarpour.shoppinglist.ui.screens.Screen
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModelInterface
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun MainListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModelInterface,
    userViewModel: UserViewModel,
    navigateToLogin: () -> Unit
) {

    val shoppingListsState = shoppingListViewModel.shoppingListsFlow.collectAsState(initial = listOf())

    val displayArchivedLists = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val appBarMenuState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text = if(displayArchivedLists.value) "Archived Lists" else "Shopping Lists")},
            navigationIcon = if (displayArchivedLists.value) {
                {
                    IconButton(onClick = {displayArchivedLists.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Show active lists"
                        )
                    }
                }
            } else null,
            actions = {

                IconButton(onClick = {
                    navController.navigate(Screen.CreateListScreen.route)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add new list")
                }

                IconButton(onClick = { appBarMenuState.value = true }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Options menu")
                }

                DropdownMenu(
                    expanded = appBarMenuState.value,
                    onDismissRequest = { appBarMenuState.value = false }
                ) {
                    // Create New List
                    DropdownMenuItem(onClick = {
                        appBarMenuState.value = false
                        navController.navigate(Screen.CreateListScreen.route)
                    }) {
                        Text("Create New List")
                    }

                    // Archived lists
                    DropdownMenuItem(onClick = {
                        displayArchivedLists.value = !displayArchivedLists.value
                        appBarMenuState.value = false
                    }) {
                        Text(text = if (displayArchivedLists.value ) "Active Lists" else "Archived Lists")
                    }

                    if (userViewModel.appMode == AppMode.ONLINE) {
                        // Sign out
                        DropdownMenuItem(onClick = {
                            appBarMenuState.value = false

                            coroutineScope.launch {
                                userViewModel.signOut()
                            }

                            navigateToLogin()

                        }) {
                            Text(text = "Sign out")
                        }
                    } else {
                        // Sign in
                        DropdownMenuItem(onClick = {
                            appBarMenuState.value = false
                            navigateToLogin()
                        }) {
                            Text(text = "Sign in")
                        }
                    }
                }
            },
            elevation = 0.dp
        )},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CreateListScreen.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Create new list")
            }
        }
    ) { padding ->
        AnimatedSurface(padding = padding) {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val allItems = shoppingListsState.value.filter { displayArchivedLists.value == it.shoppingList.isArchived }

                if (allItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .shadow(4.dp, RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .fillMaxWidth(0.75f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column( horizontalAlignment = Alignment.CenterHorizontally) {
                            if (displayArchivedLists.value) {
                                Text(
                                    text = "You have no archived list yet",
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center)
                            } else {
                                Text(
                                    text = "You have no shopping lists yet",
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "create one by clicking the + button",
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    items(allItems.size) { index ->
                        val shoppingList = allItems[index]
                        ShoppingListListItem(
                            shoppingList,
                            onItemClickListener = {navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.shoppingList.id))})
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }

        }
    }
}
