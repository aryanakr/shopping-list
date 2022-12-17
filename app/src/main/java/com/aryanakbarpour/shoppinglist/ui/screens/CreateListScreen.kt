package com.aryanakbarpour.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModelInterface
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.getTestShoppingListViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun CreateListScreen(navController: NavController, shoppingListViewModel: ShoppingListViewModelInterface, userViewModel: UserViewModel, listId: String? = null) {

    val shoppingList =
        shoppingListViewModel.shoppingListsFlow
            .collectAsState(initial = listOf())
            .value.firstOrNull { it.shoppingList.id == listId }

    val isInitialised = remember { mutableStateOf(false) }

    val showItemDialogState = remember { mutableStateOf(false) }

    val lastAddedIndex = remember { mutableStateOf(0) }


    val listNameState = remember {
        mutableStateOf(
            TextFieldValue(text = shoppingList?.shoppingList?.name ?: "")) }

    val activeCheckState = remember {
        mutableStateOf(
            shoppingList?.shoppingList?.isActive ?: false) }

    val shoppingListItems =
        if (listId != null )
            shoppingListViewModel.getShoppingListItems(listId ?: "")
                .collectAsState(initial = listOf()).value
        else
            listOf()

    val listItems = remember {mutableStateOf(listOf<ShoppingItem>())}

    if (shoppingList != null && !isInitialised.value) {
        listNameState.value = TextFieldValue(text = shoppingList.shoppingList.name)
        activeCheckState.value = shoppingList.shoppingList.isActive

        val populatedItems = mutableListOf<ShoppingItem>()
        populatedItems.addAll(listItems.value)
        populatedItems.addAll(shoppingListItems)

        listItems.value = populatedItems

        isInitialised.value = true
    }

    val editItemState = remember { mutableStateOf(ShoppingItem()) }


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
                        isActive = activeCheckState.value
                    )

                    if (listId == null) {
                        shoppingListViewModel.addShoppingList(ShoppingListWithItems(newList, listItems.value))
                    } else {
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
            if (showItemDialogState.value) {
                ShoppingItemAddEditDialog(
                    item = editItemState.value,
                    onDismiss = { showItemDialogState.value = false },
                    onAddItem = { shoppingItem ->
                        showItemDialogState.value = false
                        val newItem = shoppingItem.copy(position = lastAddedIndex.value)
                        val populatedItems = mutableListOf<ShoppingItem>()
                        populatedItems.addAll(listItems.value)

                        if (editItemState.value.position != 0) {
                            populatedItems.remove(editItemState.value)
                        }

                        populatedItems.add(newItem)
                        listItems.value = populatedItems

                        lastAddedIndex.value += 1

                        editItemState.value = ShoppingItem()

                    }
                )
            }
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
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

                    Switch(checked = activeCheckState.value, onCheckedChange = {activeCheckState.value = it})
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
                            Text(text = "Item Name", modifier = Modifier.padding(16.dp))
                            Text(text = "Quantity", modifier = Modifier.padding(16.dp))
                        }

                        LazyColumn() {
                            items(listItems.value.size) {
                                ShoppingItemListItem(
                                    listItems.value[it],
                                    onDeleteCallback = {
                                        val populatedItems = mutableListOf<ShoppingItem>()
                                        populatedItems.addAll(listItems.value)

                                        val itemIndex = populatedItems.indexOfFirst { e -> e.position == listItems.value[it].position }
                                        populatedItems.removeAt(itemIndex)
                                        listItems.value = populatedItems
                                    },
                                    onClickCallback = {
                                        editItemState.value = listItems.value[it]
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

@Composable
private fun ShoppingItemListItem(item: ShoppingItem, onDeleteCallback: () -> Unit, onClickCallback: () -> Unit) {
    val lineThickness = with(LocalDensity.current) { 3.dp.toPx() }

    Row (modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .border(3.dp, PrimaryDark, shape = getBottomLineShape(lineThickness))
        .clickable { onClickCallback() },
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDeleteCallback) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete item")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = item.name)
            Text(text = "${item.quantity} ${item.unit}")
        }
    }
}

@Composable
fun ShoppingItemAddEditDialog(
    item: ShoppingItem? = null,
    onAddItem: (ShoppingItem) -> Unit,
    onDismiss: () -> Unit
) {
    val nameState = remember {
        mutableStateOf(item?.name ?: "")
    }

    val amountState = remember {
        mutableStateOf<Double>(item?.quantity ?: 0.0)
    }

    val unitState = remember {
        mutableStateOf(item?.unit ?: "")
    }

    val nameErrorState = remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.background)
        ) {
            Column() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center) {

                    Text(text = if (item != null) "Add New Item" else "Edit Item", style = MaterialTheme.typography.h6)
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                        TextField(
                            value = nameState.value,
                            onValueChange = {nameState.value = it},
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = PrimaryDark,
                                focusedLabelColor = PrimaryDark,
                                unfocusedIndicatorColor = PrimaryDark,
                                cursorColor = PrimaryDark,
                            ),
                            label = { Text(text = "Item Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nameErrorState.value
                        )
                        if (nameErrorState.value) {
                            Text(text = "Item name cannot be empty", color = Color.Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {

                        TextField(
                            modifier = Modifier.weight(1f),
                            value = amountState.value.toString(),
                            onValueChange = {
                                try {
                                    amountState.value = it.toDouble()
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                }

                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = PrimaryDark,
                                focusedLabelColor = PrimaryDark,
                                unfocusedIndicatorColor = PrimaryDark,
                                cursorColor = PrimaryDark,
                            ),
                            label = { Text(text = "Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        TextField(
                            modifier = Modifier.weight(1f),
                            value = unitState.value,
                            onValueChange = {unitState.value = it},
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = PrimaryDark,
                                focusedLabelColor = PrimaryDark,
                                unfocusedIndicatorColor = PrimaryDark,
                                cursorColor = PrimaryDark,
                            ),
                            label = { Text(text = "Unit") },
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        Button(onClick = {
                            if (nameState.value.isEmpty()) {
                                nameErrorState.value = true
                            } else {

                                val newItem = ShoppingItem(
                                    name = nameState.value,
                                    quantity = amountState.value.toDouble(),
                                    unit = unitState.value
                                )
                                onAddItem(newItem)
                            }
                        }) {
                            Text(text = "Submit")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedButton(onClick = onDismiss) {
                            Text(text = "Cancel", color = PrimaryDark)
                        }
                    }
                }

            }
        }
    }
}
