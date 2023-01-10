package com.aryanakbarpour.shoppinglist.ui.screens.view_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.ui.screens.Screen
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel


@Composable
fun ViewListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModel,
    listId: String) {


    val shoppingList = shoppingListViewModel.shoppingListsFlow
        .collectAsState(initial = listOf())
        .value.firstOrNull { it.shoppingList.id == listId }

    val items = shoppingListViewModel.getShoppingListItems(listId).collectAsState(initial = listOf()).value


    val appBarMenuState = remember { mutableStateOf(false) }


    Scaffold (
        topBar = { TopAppBar (
            title = { Text(text = shoppingList?.shoppingList?.name ?: "") },
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back to shopping lists")
                }
            },
            actions = {
                if (shoppingList != null) {

                    IconButton(onClick = {
                        navController.navigate(Screen.EditListScreen.route + "/${shoppingList.shoppingList.id}")
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit list")
                    }

                    IconButton(onClick = { appBarMenuState.value = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Options menu")
                    }

                    DropdownMenu(
                        expanded = appBarMenuState.value,
                        onDismissRequest = { appBarMenuState.value = false }
                    ) {

                        // Edit
                        DropdownMenuItem(modifier = Modifier.width(120.dp),onClick = {
                            appBarMenuState.value = false
                            navController.navigate(Screen.EditListScreen.route + "/${shoppingList.shoppingList.id}")
                        }) {
                            Text(text = "Edit")
                        }

                        // Archive / Unarchive
                        DropdownMenuItem(modifier = Modifier.width(120.dp),onClick = {
                            appBarMenuState.value = false
                            shoppingListViewModel.archiveList(shoppingList)
                            navController.popBackStack()
                        }) {
                            Text(text = if (shoppingList.shoppingList.isArchived) "Unarchive" else "Archive")
                        }

                        // Delete
                        DropdownMenuItem(modifier = Modifier.width(120.dp),onClick = {
                            appBarMenuState.value = false
                            shoppingListViewModel.deleteShoppingList(shoppingList)
                            navController.popBackStack()
                        }) {
                            Text(text = "Delete")
                        }
                    }


                }
            }
        )}
    ) {
        Surface (
            color = Color.Transparent,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Primary,
                            PrimaryLight,
                            PrimaryLight
                        ),
                    )
                )
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
        ) {
            if (shoppingList != null) {
                Column() {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val signedItemsCount = items.count { it.collectionStatus != CollectionStatus.NOT_COLLECTED }
                        Text(text = "$signedItemsCount/${items.size} Items Marked")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                            .background(MaterialTheme.colors.surface)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                        ) {


                            val checksSize = 42.dp

                            ViewListShoppingItemHeader(checksSize)

                            LazyColumn(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                            ) {
                                items(count = items.size) { index ->
                                    ViewShoppingItemListItem(
                                        item = items[index],
                                        setItemStatus = { item: ShoppingItem, status: CollectionStatus ->
                                            val newItem = item.copy(collectionStatus = status)
                                            shoppingListViewModel.updateShoppingItem(newItem)
                                        },
                                        checkSize = checksSize,
                                    )
                                }

                            }

                        }

                    }
                }
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Column {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Loading...")
                    }
                }
            }

        }
    }
}
