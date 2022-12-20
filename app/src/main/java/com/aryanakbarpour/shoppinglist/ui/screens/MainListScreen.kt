package com.aryanakbarpour.shoppinglist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.model.AppMode
import com.aryanakbarpour.shoppinglist.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.ui.components.AnimatedSurface
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModelInterface
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.getTestShoppingListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun MainListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModelInterface,
    userViewModel: UserViewModel,
    navigateToLogin: () -> Unit
) {

    val shoppingListsState = shoppingListViewModel.shoppingListsFlow.collectAsState(initial = listOf())

    val appBarMenuState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text ="Shopping Lists")},
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
                        // Todo: Navigate to archived lists
                    }) {
                        Text(text = "Archived Lists")
                    }

                    if (userViewModel.appMode == AppMode.ONLINE) {
                        // Sign out
                        DropdownMenuItem(onClick = {
                            appBarMenuState.value = false

                            // Todo: Improve on handling sign out
                            GlobalScope.launch(Dispatchers.IO) {
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
                val allItems = shoppingListsState.value.filter { !it.shoppingList.isArchived }

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
                            Text(text = "You have no shopping lists yet", style = MaterialTheme.typography.body1, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "create one by clicking the + button", style = MaterialTheme.typography.body1, textAlign = TextAlign.Center)
                        }
                    }
                }

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    items(allItems.size) { index ->
                        val shoppingList = allItems[index]
                        ShoppingListItem(
                            shoppingList,
                            onItemClickListener = {navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.shoppingList.id!!))})
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }

        }


    }
}

@Composable
fun ShoppingListItem(shoppingList: ShoppingListWithItems, onItemClickListener: () -> Unit) {
    Box(modifier = Modifier
        .shadow(8.dp, RoundedCornerShape(16.dp))
        .width(350.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .clickable {
            onItemClickListener.invoke()
        }
        .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = shoppingList.shoppingList.name)
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (shoppingList.items.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "${shoppingList.items.filter { it.collectionStatus == CollectionStatus.COLLECTED }.size.toString()}/${shoppingList.items.size}")

            }
        }

    }

}
