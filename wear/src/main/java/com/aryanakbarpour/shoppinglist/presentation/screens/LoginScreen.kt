package com.aryanakbarpour.shoppinglist.presentation.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.core.R
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
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


    LoginDialog(
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


@Composable
fun LoginDialog(
    dialogState: MutableState<Boolean>,
    onSubmit: (email: String, password: String) -> Unit,
    signInResult: MutableState<Response<Boolean>?>,

)
{
    val emailTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val passwordTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val listState = rememberScalingLazyListState()

    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            content = {
                Scaffold(
                    timeText = {
                        TimeText()
                    },
                    positionIndicator = {
                        PositionIndicator(scalingLazyListState = listState)
                    },
                ) {

                    ScalingLazyColumn(modifier = Modifier
                        .background(PrimaryDark),
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(horizontal = 18.dp)
                    ) {

                        item {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Sign in with email",
                                fontSize = 18.sp, color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }

                        item {
                            TextField(
                                value = emailTextState.value,
                                onValueChange = { emailTextState.value = it },
                                label = { Text(text = "Email") },
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        item {
                            TextField(
                                value = passwordTextState.value,
                                onValueChange = { passwordTextState.value = it },
                                label = { Text(text = "Password") },
                            )
                        }

                        item {
                            Box(modifier = Modifier.padding(8.dp)) {
                                Button(onClick = {
                                    onSubmit(
                                        emailTextState.value.text,
                                        passwordTextState.value.text
                                    )
                                }) {
                                    if (signInResult.value is Response.Loading) {
                                        CircularProgressIndicator(indicatorColor = Color.White)
                                    } else {
                                        Text(text = "Login")
                                    }
                                }
                            }
                        }
                    }

                }

            }
        )
    }
}
