package com.aryanakbarpour.shoppinglist.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.core.R

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_google_logo
                ),
                contentDescription = null
            )
        }
        Text(
            text = "Google",
        )

    }
}

@Preview
@Composable
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onClick = {})
}
