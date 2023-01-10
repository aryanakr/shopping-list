package com.aryanakbarpour.shoppinglist.presentation.screens.login

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.core.R
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navigateToShoppingList: () -> Unit,
    launchGoogleApi: () -> Unit
) {

    Log.d("kir", "login visited")

    val dialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    // Check if the user is already signed in
    if (viewModel.isUserAuthenticated) {
        navigateToShoppingList()
    }

    val coroutineScope = rememberCoroutineScope()
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signInResult.value is Response.Success) {
        signInResult.value = null
        dialogState.value = false
        navigateToShoppingList()
    } else if (signInResult.value is Response.Failure) {
        Log.d("signin", (signInResult.value as Response.Failure).e.toString())
    }


    EmailLoginDialog(
        dialogState = dialogState,
        onSubmit =
        { email, password ->
            signInResult.value = Response.Loading
            coroutineScope.launch {
                signInResult.value = viewModel.loginWithEmail(
                    email,
                    password
                )
            }
        },
        signInResult = signInResult
    )

    Scaffold(
        timeText = {
            TimeText()
        },
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark),
            contentAlignment = Alignment.Center
        ) {

            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Sign in with", fontSize = 18.sp, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Button( modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                    onClick = {
                        dialogState.value = true
                    }
                ) {
                    if (signInResult.value is Response.Loading) {
                        CircularProgressIndicator(indicatorColor = Color.White)
                    } else {
                        Text(text = "Email")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                GoogleSignInButton( modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                    onClick = {
                        launchGoogleApi()
                    }
                )
            }
        }
    }

}
