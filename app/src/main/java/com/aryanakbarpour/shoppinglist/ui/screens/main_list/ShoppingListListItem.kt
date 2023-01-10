package com.aryanakbarpour.shoppinglist.ui.screens.main_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems

@Composable
fun ShoppingListListItem(shoppingList: ShoppingListWithItems, onItemClickListener: () -> Unit) {
    Box(modifier = Modifier
        .shadow(8.dp, RoundedCornerShape(16.dp))
        .width(350.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .clickable {
            onItemClickListener.invoke()
        }
        .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = shoppingList.shoppingList.name)
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (shoppingList.items.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "${
                            shoppingList.items
                                .filter { it.collectionStatus == CollectionStatus.COLLECTED }
                                .size}/${shoppingList.items.size}")

            }
        }

    }
}

@Preview
@Composable
fun ShoppingListListItemPreview() {
    val items = listOf(
        ShoppingItem(name = "Item 1", quantity = "1", unit = "kg", collectionStatus = CollectionStatus.COLLECTED),
        ShoppingItem(name = "Item 2", quantity = "1", unit = "kg", collectionStatus = CollectionStatus.COLLECTED),
        ShoppingItem(name = "Item 3", quantity = "1", unit = "kg", collectionStatus = CollectionStatus.MISSING),
    )
    val shoppingList = ShoppingListWithItems(shoppingList = ShoppingList(name = "List 1"), items = items)
    ShoppingListListItem(shoppingList = shoppingList) {}
}