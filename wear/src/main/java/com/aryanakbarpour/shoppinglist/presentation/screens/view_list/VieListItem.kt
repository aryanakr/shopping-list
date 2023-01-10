package com.aryanakbarpour.shoppinglist.presentation.screens.view_list

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem


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

@Preview
@Composable
fun ViewListItemPreview() {
    ViewListItem(item = ShoppingItem(name = "Item Name"), onClick = {})
}