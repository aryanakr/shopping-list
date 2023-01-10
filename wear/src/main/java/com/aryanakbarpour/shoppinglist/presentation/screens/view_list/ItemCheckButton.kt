package com.aryanakbarpour.shoppinglist.presentation.screens.view_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus


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

@Preview
@Composable
fun ItemCheckButtonPreview() {
    ItemCheckButton(itemState = CollectionStatus.COLLECTED)
}