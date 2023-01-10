package com.aryanakbarpour.shoppinglist.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark


@Composable
fun SignUpForm(
    nameTextState: MutableState<TextFieldValue>,
    emailTextState: MutableState<TextFieldValue>,
    passwordTextState: MutableState<TextFieldValue>,
    emailErrorState: MutableState<String?>,
    passwordErrorState: MutableState<String?>,
    signUpResult: MutableState<Response<Boolean>?>,
    onSubmit: () -> Unit
) {
    Box( modifier = Modifier
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp),
                color = PrimaryDark,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nameTextState.value,
                label = { Text(text = "Name (Optional)") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = PrimaryDark,
                    focusedLabelColor = PrimaryDark,
                    unfocusedIndicatorColor = PrimaryDark,
                    cursorColor = PrimaryDark,
                ),
                onValueChange = {
                    nameTextState.value = it
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
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
                    isError = emailErrorState.value != null,
                    onValueChange = {
                        emailTextState.value = it
                    }
                )
                if (emailErrorState.value != null) {
                    Text(
                        text = emailErrorState.value!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
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
                    isError = passwordErrorState.value != null,
                    onValueChange = {
                        passwordTextState.value = it
                    }
                )
                if (passwordErrorState.value != null) {
                    Text(
                        text = passwordErrorState.value!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(32.dp),
                onClick = onSubmit
            ) {
                if (signUpResult.value is Response.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Create Account")
                }
            }
        }
    }
}

@Preview
@Composable
fun SignUpFormPreview() {
    val nameTextState = remember {mutableStateOf(TextFieldValue())}
    val emailTextState = remember {mutableStateOf(TextFieldValue())}
    val passwordTextState = remember {mutableStateOf(TextFieldValue())}
    val emailErrorState = remember {mutableStateOf<String?>(null)}
    val passwordErrorState = remember {mutableStateOf<String?>(null)}
    val signUpResult = remember {mutableStateOf<Response<Boolean>?>(null)}

    SignUpForm(
        nameTextState = nameTextState,
        emailTextState = emailTextState,
        passwordTextState = passwordTextState,
        emailErrorState = emailErrorState,
        passwordErrorState = passwordErrorState,
        signUpResult = signUpResult,
        onSubmit = {}
    )
}