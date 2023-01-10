package com.aryanakbarpour.shoppinglist.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark


@Composable
fun EmailLoginDialog(
    dialogState: MutableState<Boolean>,
    onSubmit: (email: String, password: String) -> Unit,
    signInResult: MutableState<Response<Boolean>?>,

    )
{
    val emailTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val passwordTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val listState = rememberScalingLazyListState()

    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            content = {
                Scaffold(
                    timeText = {
                        TimeText()
                    },
                    positionIndicator = {
                        PositionIndicator(scalingLazyListState = listState)
                    },
                ) {

                    ScalingLazyColumn(modifier = Modifier
                        .background(PrimaryDark),
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(horizontal = 18.dp)
                    ) {

                        item {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Sign in with email",
                                fontSize = 18.sp, color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }

                        item {
                            TextField(
                                value = emailTextState.value,
                                onValueChange = { emailTextState.value = it },
                                label = { Text(text = "Email") },
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        item {
                            TextField(
                                value = passwordTextState.value,
                                onValueChange = { passwordTextState.value = it },
                                label = { Text(text = "Password") },
                            )
                        }

                        item {
                            Box(modifier = Modifier.padding(8.dp)) {
                                Button(onClick = {
                                    onSubmit(
                                        emailTextState.value.text,
                                        passwordTextState.value.text
                                    )
                                }) {
                                    if (signInResult.value is Response.Loading) {
                                        CircularProgressIndicator(indicatorColor = Color.White)
                                    } else {
                                        Text(text = "Login")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}