package com.aryanakbarpour.shoppinglist.ui.screens.create_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight

@Composable
fun AddEditShoppingItemDialog(
    item: ShoppingItem? = null,
    onSubmit: (ShoppingItem) -> Unit,
    onDismiss: () -> Unit
) {
    val nameState = remember {
        mutableStateOf(item?.name ?: "")
    }

    val amountState = remember {
        mutableStateOf<String>(item?.quantity ?: "")
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
                // Create top header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center) {

                    Text(text = if (item != null) "Edit Item" else "Add New Item", style = MaterialTheme.typography.h6)
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Item Name text field
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

                        Text(text =
                            if (nameErrorState.value) "Item name cannot be empty"
                            else ""
                            , color = Color.Red)

                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {

                        TextField(
                            modifier = Modifier.weight(1f),
                            value = amountState.value.toString(),
                            onValueChange = {
                                try {
                                    amountState.value = it
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

                                val newItem = if (item != null) item.copy(
                                    name = nameState.value,
                                    quantity = amountState.value,
                                    unit = unitState.value)
                                else ShoppingItem(
                                    name = nameState.value,
                                    quantity = amountState.value,
                                    unit = unitState.value
                                )
                                onSubmit(newItem)
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

@Preview
@Composable
fun AddEditShoppingItemDialogPreview() {
    val item = ShoppingItem ( name = "Rice", quantity = "1", unit = "kg")

    AddEditShoppingItemDialog(item, {}, {})
}
