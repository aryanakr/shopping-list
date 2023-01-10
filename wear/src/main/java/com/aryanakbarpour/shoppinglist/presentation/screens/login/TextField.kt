package com.aryanakbarpour.shoppinglist.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary

@Composable
fun TextField(modifier: Modifier = Modifier, label: @Composable () -> Unit, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Column {
        label()
        Spacer(modifier = modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(2.dp, Primary)
                .padding(16.dp),
            maxLines = 1

        )
    }
}

@Preview
@Composable
fun TextFieldPreview() {
    TextField(
        label = {Text ("Label")},
        value = TextFieldValue(""),
        onValueChange = {}
    )
}