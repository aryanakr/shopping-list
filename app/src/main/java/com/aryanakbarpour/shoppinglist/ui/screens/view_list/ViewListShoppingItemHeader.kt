package com.aryanakbarpour.shoppinglist.ui.screens.view_list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun ViewListShoppingItemHeader (
    checksSize: Dp
) {

    val headerLineThickness = with(LocalDensity.current) { 3.dp.toPx() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                3.dp,
                Color.Gray,
                shape = getBottomLineShape(headerLineThickness)
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(checksSize), contentAlignment = Alignment.Center) {
            FaIcon(faIcon = FaIcons.Times)
        }

        Text(text = "Item Name", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Amount", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

        Box(modifier = Modifier.width(checksSize), contentAlignment = Alignment.Center) {
            FaIcon(faIcon = FaIcons.Check)
        }

    }
}

@Preview
@Composable
fun ViewListShoppingItemHeaderPreview() {
    ViewListShoppingItemHeader(checksSize = 64.dp)
}