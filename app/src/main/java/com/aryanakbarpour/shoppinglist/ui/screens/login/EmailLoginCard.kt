package com.aryanakbarpour.shoppinglist.ui.screens.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark

@Composable
fun EmailLoginCard(
    emailTextState: MutableState<TextFieldValue>,
    passwordTextState: MutableState<TextFieldValue>,
    errorState: MutableState<String?>,
    signInResult: MutableState<Response<Boolean>?>,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .border(
            1.dp,
            PrimaryDark,
            shape = MaterialTheme.shapes.medium
        )
        .padding(horizontal = 8.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = emailTextState.value,
                label = { Text(text = "Email") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = PrimaryDark,
                    focusedLabelColor = PrimaryDark,
                    unfocusedIndicatorColor = PrimaryDark,
                    cursorColor = PrimaryDark,
                ),
                isError = errorState.value != null,
                onValueChange = {
                    emailTextState.value = it
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordTextState.value,
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = PrimaryDark,
                    focusedLabelColor = PrimaryDark,
                    unfocusedIndicatorColor = PrimaryDark,
                    cursorColor = PrimaryDark,
                ),
                isError = errorState.value != null,
                onValueChange = {
                    passwordTextState.value = it
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            Text(text = errorState.value ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Center)

            // Login Button
            Button(onClick = {
                onLoginClicked()
            }) {
                if (signInResult.value is Response.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Login", fontSize = 18.sp)
                }
            }

            // Sign up button
            TextButton(
                onClick = { onRegisterClicked() },
                modifier = Modifier.padding(8.dp))
            {
                Text(text = "Create New Account", color = PrimaryDark, fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun EmailLoginCardPreview() {
    val emailState = remember { mutableStateOf(TextFieldValue(text = "")) }
    val passwordState = remember { mutableStateOf(TextFieldValue(text = "")) }
    val errorState = remember { mutableStateOf<String?>("Error message") }
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    Surface(color = Color.White) {
        EmailLoginCard(
            emailTextState = emailState,
            passwordTextState = passwordState,
            errorState = errorState,
            signInResult = signInResult,
            onLoginClicked = {},
            onRegisterClicked = {}
        )
    }
}