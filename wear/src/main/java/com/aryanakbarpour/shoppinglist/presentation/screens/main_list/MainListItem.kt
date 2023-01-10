package com.aryanakbarpour.shoppinglist.presentation.screens.main_list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList


@Composable
fun MainListItem(shoppingList: ShoppingList, onClick: () -> Unit) {

    Chip(modifier = Modifier
        .fillMaxWidth(),
        label = {
            Text(
                text = shoppingList.name,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center)
        },
        onClick = { onClick() })
}

@Preview
@Composable
fun MainListItemPreview() {
    MainListItem(shoppingList = ShoppingList(name = "List Name"), onClick = {})
}
