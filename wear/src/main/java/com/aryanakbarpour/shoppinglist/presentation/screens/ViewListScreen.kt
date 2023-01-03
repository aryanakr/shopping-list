package com.aryanakbarpour.shoppinglist.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel

@Composable
fun ViewListScreen(navController: NavController, viewModel: ShoppingListViewModel, listId: String) {
    val shoppingItems = viewModel.getShoppingListItems(listId).collectAsState(initial = listOf())

    val shoppingListName = viewModel.shoppingListsFlow.collectAsState(initial = listOf()).value
        .find { it.shoppingList.id == listId }?.shoppingList?.name

    Box(modifier = Modifier
        .fillMaxSize()
        .background(PrimaryDark),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            shoppingListName?.let { title ->
                item {
                    ViewListHeader(title)
                }
            }

            items(shoppingItems.value.size) { index ->
                val shoppingItem = shoppingItems.value[index]
                ViewListItem(shoppingItem) {
                    val newState = when (shoppingItem.collectionStatus) {
                        CollectionStatus.COLLECTED -> CollectionStatus.MISSING
                        CollectionStatus.MISSING -> CollectionStatus.NOT_COLLECTED
                        CollectionStatus.NOT_COLLECTED -> CollectionStatus.COLLECTED
                    }
                    viewModel.updateShoppingItem(shoppingItem.copy(collectionStatus = newState))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ViewListHeader(title: String) {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = TextStyle(fontSize = 16.sp))
    }
}

@Composable
fun ViewListItem(item: ShoppingItem, onClick: () -> Unit) {
    Box (modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = Color.Gray)
        .clickable {
            onClick()
        }
        .padding(horizontal = 8.dp)
    ) {
        Row (modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Column(modifier = Modifier
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = item.name)

                val amountText = "${item.quantity} ${item.unit}"
                if (amountText.isNotBlank())
                    Text(text = "${item.quantity} ${item.unit}")
            }

            ItemCheckButton(item.collectionStatus)

        }
    }
}

@Composable
fun ItemCheckButton(itemState: CollectionStatus) {

    val checkColour = when (itemState) {
        CollectionStatus.COLLECTED -> Color.Green
        CollectionStatus.NOT_COLLECTED -> Color.White
        CollectionStatus.MISSING -> Color.Red
    }
    Box (modifier = Modifier
        .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(checkColour)
                .border(2.dp, Color.Black, CircleShape)
        )
    }
}