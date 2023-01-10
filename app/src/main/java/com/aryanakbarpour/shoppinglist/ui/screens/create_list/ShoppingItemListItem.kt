package com.aryanakbarpour.shoppinglist.ui.screens.create_list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape

@Composable
fun ShoppingItemListItem(item: ShoppingItem, onDeleteCallback: () -> Unit, onClickCallback: () -> Unit) {
    val lineThickness = with(LocalDensity.current) { 3.dp.toPx() }

    Row (modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .border(3.dp, PrimaryDark, shape = getBottomLineShape(lineThickness))
        .clickable { onClickCallback() },
        verticalAlignment = Alignment.CenterVertically) {


        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(0.1f), contentAlignment = Alignment.Center) {
                IconButton(onClick = onDeleteCallback) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete item")
                }
            }

            Text(modifier = Modifier.weight(0.45f),text = item.name, textAlign = TextAlign.Center)
            Text(modifier = Modifier.weight(0.45f), text = "${item.quantity} ${item.unit}", textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun ShoppingItemListItemPreview() {
    Surface(color = Color.White) {
        ShoppingItemListItem(
            item = ShoppingItem(name = "Milk", quantity =  "1", unit = "pint"),
            onDeleteCallback = {}, onClickCallback = {})
    }
}
