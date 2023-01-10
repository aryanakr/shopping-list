package com.aryanakbarpour.shoppinglist.ui.screens.create_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModelInterface
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape
import com.aryanakbarpour.shoppinglist.viewmodel.TestShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun CreateListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModelInterface,
    listId: String? = null
) {

    val shoppingList =
        shoppingListViewModel.shoppingListsFlow
            .collectAsState(initial = listOf())
            .value.firstOrNull { it.shoppingList.id == listId }

    val showItemDialogState = remember { mutableStateOf(false) }

    val listNameState = remember {
        mutableStateOf(
            TextFieldValue(text = shoppingList?.shoppingList?.name ?: "")) }


    val shoppingListItems =
        if (listId != null )
            shoppingListViewModel.getShoppingListItems(listId ?: "")
                .collectAsState(initial = listOf()).value
        else
            listOf()

    val listItems = remember {mutableStateOf(listOf<ShoppingItem>())}

    val deletedItems = remember { mutableStateOf(listOf<ShoppingItem>()) }


    // initialising the screen for edit
    val isInitialised = remember { mutableStateOf(false) }
    if (shoppingList != null && !isInitialised.value) {
        listNameState.value = TextFieldValue(text = shoppingList.shoppingList.name)

        val populatedItems = mutableListOf<ShoppingItem>()
        populatedItems.addAll(listItems.value)
        populatedItems.addAll(shoppingListItems)

        listItems.value = populatedItems

        isInitialised.value = true
    }

    val editItemIndex = remember { mutableStateOf(-1) }


    Scaffold (
        topBar = { TopAppBar(
            title = { Text(text = if (listId == null) "Create List" else "Edit List") },
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back to shopping lists")
                }
            },
            actions = {
                IconButton(onClick = {
                    val newList = ShoppingList(
                        id = listId ?: "",
                        name = listNameState.value.text,
                    )

                    if (listId == null) {
                        shoppingListViewModel.addShoppingList(ShoppingListWithItems(newList, listItems.value))
                    } else {
                        shoppingListViewModel.deleteShoppingItems(deletedItems.value)
                        shoppingListViewModel.updateShoppingList(ShoppingListWithItems(newList, listItems.value))
                    }

                    navController.popBackStack()
                }) {
                    FaIcon(faIcon = FaIcons.Save)
                }
            }
        )},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showItemDialogState.value = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Create new list")
            }
        }

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

            // Handle displaying the create item dialogue
            if (showItemDialogState.value) {
                val editItem = if (editItemIndex.value != -1) listItems.value[editItemIndex.value] else null
                AddEditShoppingItemDialog(
                    item = editItem,
                    onDismiss = { showItemDialogState.value = false },
                    onSubmit = { shoppingItem ->
                        showItemDialogState.value = false

                        val populatedItems = mutableListOf<ShoppingItem>()
                        populatedItems.addAll(listItems.value)

                        if (editItemIndex.value != -1) {
                            populatedItems.removeAt(editItemIndex.value)
                        }

                        populatedItems.add(shoppingItem)
                        listItems.value = populatedItems

                        editItemIndex.value = -1

                    }
                )
            }


            Column {
                // create list name text field
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)) {
                    TextField(
                        value = listNameState.value,
                        onValueChange = {listNameState.value = it},
                        label = { Text(text = "List Name") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryDark,
                            focusedLabelColor = PrimaryDark,
                            unfocusedIndicatorColor = PrimaryDark,
                            cursorColor = PrimaryDark,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                    .background(Color.White)) {
                    
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()) {

                        val lineThickness = with(LocalDensity.current) { 3.dp.toPx() }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(3.dp, Color.Gray, shape = getBottomLineShape(lineThickness)),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            Spacer(modifier = Modifier.weight(0.1f))
                            Text(text = "Item Name", modifier = Modifier
                                .weight(0.45f)
                                .padding(16.dp), textAlign = TextAlign.Center)
                            Text(text = "Quantity", modifier = Modifier
                                .weight(0.45f)
                                .padding(16.dp), textAlign = TextAlign.Center)
                        }

                        LazyColumn() {
                            items(listItems.value.size) { index ->
                                ShoppingItemListItem(
                                    listItems.value[index],
                                    onDeleteCallback = {

                                        if (listItems.value[index].id.isNotBlank()) {
                                            deletedItems.value = deletedItems.value + listItems.value[index]
                                        }

                                        val populatedItems = mutableListOf<ShoppingItem>()

                                        populatedItems.addAll(listItems.value)
                                        populatedItems.removeAt(index)

                                        listItems.value = populatedItems
                                    },
                                    onClickCallback = {
                                        editItemIndex.value = index
                                        showItemDialogState.value = true
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }

    }
}

@Preview
@Composable
fun CreateListScreenPreview () {
    val viewModel = TestShoppingListViewModel()
    CreateListScreen(
        navController = rememberNavController(),
        shoppingListViewModel = viewModel,
        listId = null
    )
}
