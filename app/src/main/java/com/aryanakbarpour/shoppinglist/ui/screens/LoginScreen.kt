package com.aryanakbarpour.shoppinglist.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.aryanakbarpour.shoppinglist.model.Response
import com.aryanakbarpour.shoppinglist.model.Response.*
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    navigateToShoppingList: () -> Unit
) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                viewModel.signInWithGoogle(googleCredentials)
            } catch (it: ApiException) {
                print(it)
            }
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    val emailTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val passwordTextState = remember { mutableStateOf(
        TextFieldValue(text = "")
    ) }

    val emailErrorState = remember {
        mutableStateOf<String?>(null)
    }

    val passwordErrorState = remember {
        mutableStateOf<String?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signInResult.value is Success) {
        signInResult.value = null
        navigateToShoppingList()
    }

    if (signInResult.value is Failure) {
        if (emailTextState.value.text.isBlank()) {
            emailErrorState.value = "Can not be empty"
        } else if ((signInResult.value as Failure).e is FirebaseAuthInvalidCredentialsException) {
            emailErrorState.value = "Invalid email"
        } else if ((signInResult.value as Failure).e is FirebaseAuthInvalidUserException) {
            emailErrorState.value = "Email or password is incorrect"
        } else {
            emailErrorState.value = null
        }

        if (passwordTextState.value.text.isBlank()) {
            passwordErrorState.value = "Can not be empty"
        } else if ((signInResult.value as Failure).e is FirebaseAuthInvalidUserException) {
             passwordErrorState.value = "Email or password is incorrect"
        } else {
            passwordErrorState.value = null
        }
    }else {
        emailErrorState.value = null
        passwordErrorState.value = null
    }



    Scaffold(
        topBar = {
            AuthTopBar()
        },
        content = { padding ->
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Primary,
                                PrimaryLight,
                                PrimaryLight
                            ),
                        )
                    )
                    .fillMaxSize()
                    .padding(padding)
            ) {
                
                Box(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(modifier = Modifier
                        .fillMaxWidth()) {
                        Column(modifier = Modifier
                            .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Login",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp),
                                color = PrimaryDark,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

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
                                        modifier = Modifier.padding(8.dp)
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
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Button(onClick = {
                                signInResult.value = Loading
                                coroutineScope.launch {
                                    signInResult.value = viewModel.loginWithEmail(
                                        emailTextState.value.text,
                                        passwordTextState.value.text
                                    )
                                }
                            }) {
                                if (signInResult.value is Loading) {
                                    CircularProgressIndicator(color = Color.White)
                                } else {
                                    Text(text = "Login")
                                }
                            }

                            Button(onClick = { navController.navigate(Screen.SignUpScreen.route)}) {
                                Text(text = "Create New Account")
                            }

                            GoogleSignInComponent(viewModel, {i -> launch(i)} , navigateToShoppingList)

                            Button(onClick = { navigateToShoppingList() }) {
                                Text(text = "Continue Offline")
                            }


                        }
                    }
                }



            }
        }
    )


}

@Composable
fun GoogleSignInComponent(viewModel: AuthViewModel, launch: (BeginSignInResult) -> Unit, navigateToProfileScreen: () -> Unit) {
    Row() {
        OneTapSignIn(
            viewModel = viewModel,
            launch = {
                launch(it)
            }
        )

        SignInWithGoogle(
            viewModel = viewModel,
            navigateToHomeScreen = { signedIn ->
                if (signedIn) {
                    navigateToProfileScreen()
                }
            }
        )

        AuthContent(
            oneTapSignIn = {
                viewModel.oneTapSignIn()
            }
        )
    }
}

@Composable
fun AuthTopBar() {
    TopAppBar (
        title = {
            Text(
                text = "Shopping List"
            )
        }
    )
}

@Composable
fun AuthContent(
    oneTapSignIn: () -> Unit
) {
    Box(
    ) {
        SignInButton(
            onClick = oneTapSignIn
        )
    }
}

@Composable
fun OneTapSignIn(
    viewModel: AuthViewModel,
    launch: (result: BeginSignInResult) -> Unit
) {
    when(val oneTapSignInResponse = viewModel.oneTapSignInResponse) {
        is Loading -> CircularProgressIndicator()
        is Success -> oneTapSignInResponse.data?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Failure -> LaunchedEffect(Unit) {
            print(oneTapSignInResponse.e)
        }
    }
}

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        FaIcon(faIcon = FaIcons.Google, size = 24.dp)
        Text(
            text = "Sign in with Google",
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}

@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel,
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Loading -> CircularProgressIndicator()
        is Success -> signInWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn)
            }
        }
        is Failure -> LaunchedEffect(Unit) {
            print(signInWithGoogleResponse.e)
        }
    }
}