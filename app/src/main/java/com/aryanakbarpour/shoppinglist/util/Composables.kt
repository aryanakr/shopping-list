package com.aryanakbarpour.shoppinglist.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

fun getBottomLineShape(bottomLineThickness: Float) : Shape {
    return GenericShape { size, _ ->
        // 1) Bottom-left corner
        moveTo(0f, size.height)
        // 2) Bottom-right corner
        lineTo(size.width, size.height)
        // 3) Top-right corner
        lineTo(size.width, size.height - bottomLineThickness)
        // 4) Top-left corner
        lineTo(0f, size.height - bottomLineThickness)
    }
}

@Composable
fun CircleCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    color: Color,
    size: Int,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier
        .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center

    ) {
        // outer circle
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .border(2.dp, color, CircleShape)
        )

        // inner circle
        if (checked) {
            Box(
                modifier = Modifier
                    .size((size - 12).dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
    
}
