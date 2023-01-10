package com.aryanakbarpour.shoppinglist.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.ui.components.AnimatedSurface
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    navigateToShoppingList: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val signUpResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    val emailTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val passwordTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val nameTextState = remember {
        mutableStateOf(TextFieldValue(text = ""))
    }

    if (signUpResult.value is Response.Success) {
        navigateToShoppingList()
    }

    val emailErrorState = remember {mutableStateOf<String?>(null)}
    val passwordErrorState = remember {mutableStateOf<String?>(null)}

    if (signUpResult.value is Response.Failure) {
        val error = signUpResult.value as Response.Failure

        if (emailTextState.value.text.trim().isBlank()) {
          emailErrorState.value = "Email is required"
        } else if (error.e is FirebaseAuthInvalidCredentialsException) {
            emailErrorState.value = "Invalid Email Address"
        } else if (error.e is FirebaseAuthUserCollisionException) {
            emailErrorState.value = "Email already exists"
        } else {
            emailErrorState.value = null
        }

        if (passwordTextState.value.text.length < 8) {
            passwordErrorState.value = "Password must be at least 8 characters"
        } else {
            passwordErrorState.value = null
        }
    } else {
        emailErrorState.value = null
        passwordErrorState.value = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back to shopping lists")
                    }
                },
                backgroundColor = Primary,
                elevation = 0.dp
            )
        }
    ) {  padding ->
        AnimatedSurface(
            padding = padding,
        ) {
            Box(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Card() {
                    SignUpForm(
                        emailTextState = emailTextState,
                        passwordTextState = passwordTextState,
                        nameTextState = nameTextState,
                        emailErrorState = emailErrorState,
                        passwordErrorState = passwordErrorState,
                        signUpResult = signUpResult,
                        onSubmit = {
                            signUpResult.value = Response.Loading
                            coroutineScope.launch {
                                val result = viewModel.signUpWithEmail(
                                    email = emailTextState.value.text,
                                    password = passwordTextState.value.text,
                                    name = nameTextState.value.text
                                )
                                signUpResult.value = result
                            }
                        }
                    )
                }
            }
        }
    }
}
