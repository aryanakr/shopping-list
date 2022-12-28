package com.aryanakbarpour.shoppinglist.ui.screens

import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.util.CircleCheckBox
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons


@Composable
fun ViewListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModel,
    userViewModel: UserViewModel,
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
                        // Share
                        DropdownMenuItem(onClick = {
                            appBarMenuState.value = false
                            // TODO: Share list
                        }) {
                            Text(text = "Share")
                        }

                        // Edit
                        DropdownMenuItem(modifier = Modifier.width(120.dp),onClick = {
                            appBarMenuState.value = false
                            navController.navigate(Screen.EditListScreen.route + "/${shoppingList.shoppingList.id}")
                        }) {
                            Text(text = "Edit")
                        }

                        // Archive
                        DropdownMenuItem(modifier = Modifier.width(120.dp),onClick = {
                            appBarMenuState.value = false
                            shoppingListViewModel.archiveList(shoppingList)
                            navController.popBackStack()
                        }) {
                            Text(text = "Archive")
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

                            val headerLineThickness = with(LocalDensity.current) { 3.dp.toPx() }
                            val checksSize = 42.dp
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .border(
                                        3.dp,
                                        Color.Gray,
                                        shape = getBottomLineShape(headerLineThickness)
                                    ),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.width(checksSize), contentAlignment = Alignment.Center) {
                                    FaIcon(faIcon = FaIcons.Times)
                                }

                                Text(text = "Item Name", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                Text(text = "Amount", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                                Box(modifier = Modifier.width(checksSize), contentAlignment = Alignment.Center) {
                                    FaIcon(faIcon = FaIcons.Check)
                                }

                            }

                            LazyColumn(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                            ) {
                                items(count = items.size) { index ->
                                    ViewShoppingListItem(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ViewShoppingListItem(
    item: ShoppingItem,
    setItemStatus: (ShoppingItem, CollectionStatus) -> Unit,
    checkSize: Dp
) {
    val lineThickness = with(LocalDensity.current) { 2.dp.toPx() }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    setItemStatus(item, CollectionStatus.MISSING)
                    false
                }
                DismissValue.DismissedToStart -> {
                    setItemStatus(item, CollectionStatus.COLLECTED)
                    false
                }
                else -> false
            }
        }
    )

    
    SwipeToDismiss(

        state = dismissState ,
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart,
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.25f)
        },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Red
                    DismissValue.DismissedToStart -> Color.Green
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> FaIcons.Times
                DismissDirection.EndToStart -> FaIcons.Check
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                FaIcon(
                    icon,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent =  {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .background(Color.White)
                    .border(2.dp, PrimaryDark, shape = getBottomLineShape(lineThickness)),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // is missing checkbox
                Box(modifier = Modifier.width(checkSize), contentAlignment = Alignment.Center) {
                    CircleCheckBox(
                        checked = item.collectionStatus == CollectionStatus.MISSING,
                        onCheckedChange = {value ->
                            setItemStatus(item, if (value) CollectionStatus.MISSING else CollectionStatus.NOT_COLLECTED)
                        },
                        color = Color.Red,
                        size = (checkSize.value - 8).toInt())
                }

                Text(text = item.name, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "${item.quantity} ${item.unit}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                // is collected checkbox
                Box(modifier = Modifier.width(checkSize), contentAlignment = Alignment.Center) {
                    CircleCheckBox(
                        checked = item.collectionStatus == CollectionStatus.COLLECTED,
                        onCheckedChange = {value ->
                            setItemStatus(item, if (value) CollectionStatus.COLLECTED else CollectionStatus.NOT_COLLECTED)
                        },
                        color = Color.Green,
                        size = (checkSize.value - 8).toInt())
                }
            }
        }

    )


}

//@Preview
//@Composable
//fun ViewListScreenPreview() {
//    ViewListScreen()
//}