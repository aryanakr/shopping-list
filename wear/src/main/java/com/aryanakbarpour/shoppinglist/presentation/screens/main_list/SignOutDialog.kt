package com.aryanakbarpour.shoppinglist.presentation.screens.main_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary


@Composable
fun SignOutDialog(
    dialogState: MutableState<Boolean>,
    onSubmit: () -> Unit
) {
    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false },
            content = {
                Box(modifier = Modifier
                    .background(Color.Black),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            text = "Are you sure you want to sign out?",
                            style = TextStyle(color = Color.White, fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row {
                            Text(text = "Cancel" , style = TextStyle(color = Primary, fontSize = 16.sp), modifier = Modifier.clickable {
                                dialogState.value = false
                            })
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "Sign Out" , style = TextStyle(color = Primary, fontSize = 16.sp), modifier = Modifier.clickable {
                                onSubmit()
                            })
                        }
                    }
                }

            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}
