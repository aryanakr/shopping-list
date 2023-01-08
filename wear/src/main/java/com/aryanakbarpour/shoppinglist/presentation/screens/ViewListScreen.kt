package com.aryanakbarpour.shoppinglist.presentation.screens

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.presentation.theme.Background
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ViewListScreen(navController: NavController, viewModel: ShoppingListViewModel, listId: String) {

    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val shoppingItems = viewModel.getShoppingListItems(listId).collectAsState(initial = listOf())

    val shoppingListName = viewModel.shoppingListsFlow.collectAsState(initial = listOf()).value
        .find { it.shoppingList.id == listId }?.shoppingList?.name

    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText()
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {

        val focusRequester = remember { FocusRequester() }

        ScalingLazyColumn(modifier = Modifier
            .fillMaxWidth()
            .background(Background)
            .onRotaryScrollEvent {
                coroutineScope.launch {
                    listState.scrollBy(it.verticalScrollPixels)
                    // do short vibration
                    if (localContext
                            .getSystemService(Vibrator::class.java)
                            .hasVibrator()
                    ) {
                        localContext
                            .getSystemService(Vibrator::class.java)
                            .vibrate(
                                VibrationEffect.createOneShot(
                                    10,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                    }
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            shoppingListName?.let { title ->
                item {
                    val collectedCount = shoppingItems.value.count { it.collectionStatus == CollectionStatus.COLLECTED }
                    ViewListHeader(title, "${collectedCount}/${shoppingItems.value.size}")
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
            }
        }

        LaunchedEffect(Unit) {focusRequester.requestFocus()}
    }
}

@Composable
fun ViewListHeader(title: String, countsText: String) {
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = title, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$countsText Collected", style = TextStyle(fontSize = 16.sp))
    }
}

@Composable
fun ViewListItem(item: ShoppingItem, onClick: () -> Unit) {
    Chip (modifier = Modifier
        .fillMaxWidth(),
        label = {
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
        },
        onClick = onClick
    )
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