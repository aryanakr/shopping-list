package com.aryanakbarpour.shoppinglist.presentation.screens.view_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text


@Composable
fun ViewListHeader(title: String, countsText: String) {
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = title, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$countsText Collected", style = TextStyle(fontSize = 16.sp))
    }
}

@Preview
@Composable
fun ViewListHeaderPreview() {
    ViewListHeader(title = "List Name", countsText = "2/5")
}
