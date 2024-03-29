package com.aryanakbarpour.shoppinglist.presentation.screens.main_list

import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.presentation.screens.Screen
import com.aryanakbarpour.shoppinglist.presentation.theme.Background
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainListScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    shoppingListViewModel: ShoppingListViewModel,
    exit: () -> Unit) {

    val localContext = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val signOutResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signOutResult.value is Response.Success) {
        signOutResult.value = null
        exit()
    }

    val shoppingListsState = shoppingListViewModel.shoppingListsFlow.collectAsState(initial = listOf())

    val listState = rememberScalingLazyListState()

    val dialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    SignOutDialog(dialogState = dialogState) {
        coroutineScope.launch {
            signOutResult.value = userViewModel.signOut()
        }
    }

    BackHandler() {
        dialogState.value = true
    }

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
            contentPadding = PaddingValues(horizontal = 24.dp),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(56.dp))
            }

            items(shoppingListsState.value.size) { index ->
                val shoppingList = shoppingListsState.value[index].shoppingList
                MainListItem(shoppingList = shoppingList) {
                    navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.id))
                }
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                Button(onClick = {
                    Log.d("sign out", "sign out")
                    dialogState.value = true
                }) {
                    FaIcon(FaIcons.SignOutAlt)
                }
            }
        }

        LaunchedEffect(Unit) {focusRequester.requestFocus()}

    }
}

