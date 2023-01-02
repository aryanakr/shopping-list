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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.aryanakbarpour.shoppinglist.R
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.presentation.theme.Primary
import com.aryanakbarpour.shoppinglist.presentation.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navigateToShoppingList: () -> Unit,
    launchGoogleApi: () -> Unit
) {
    val context = LocalContext.current

    // Check if the user is already signed in
    if (viewModel.isUserAuthenticated) {
        navigateToShoppingList()
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                Log.d("signin", "try sign in")
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                viewModel.signInWithGoogle(googleCredentials)
            } catch (it: ApiException) {
                Log.d("signin", it.toString())
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

    val coroutineScope = rememberCoroutineScope()
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signInResult.value is Response.Success) {
        signInResult.value = null
        navigateToShoppingList()
    } else if (signInResult.value is Response.Failure) {
        Log.d("signin", (signInResult.value as Response.Failure).e.toString())
    }

    Box(modifier = Modifier.background(Primary)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column() {
                Card(onClick = { /*TODO*/ }) {
                    Column() {
                        TextField(
                            value = emailTextState.value,
                            onValueChange = { emailTextState.value = it },
                            label = { Text(text = "Email") },
                        )

                        TextField(
                            value = passwordTextState.value,
                            onValueChange = { passwordTextState.value = it },
                            label = { Text(text = "Password") },
                        )
                    }

                }
            }



            Button(onClick = {
                signInResult.value = Response.Loading
                coroutineScope.launch {
                    signInResult.value = viewModel.loginWithEmail(
                        emailTextState.value.text,
                        passwordTextState.value.text
                    )
                }
            }) {
                if (signInResult.value is Response.Loading) {
                    CircularProgressIndicator(indicatorColor = Color.White)
                } else {
                    Text(text = "Login")
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                //GoogleSignInComponent(viewModel, { i -> launch(i) }, navigateToShoppingList)
                Button(onClick = {

                    launchGoogleApi()
                }) {
                    Text(text = "google")
                }

            }

        }
    }


}

@Composable
fun TextField(modifier: Modifier? = null, label: @Composable () -> Unit, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Column {
        label()
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, PrimaryDark)
                .padding(16.dp)

        )
    }
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
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> oneTapSignInResponse.data?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.d("signin", oneTapSignInResponse.e.toString())
            print(oneTapSignInResponse.e)
        }
    }
}

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier,
    ) {
//        Box(modifier = Modifier
//            .background(color = Color.White)
//            .padding(8.dp)
//        ) {
//            Image(
//                painter = painterResource(
//                    id = R.drawable.ic_google_logo
//                ),
//                contentDescription = null
//            )
//        }
        Text(
            text = "Sign in with Google",
            modifier = Modifier,
        )
    }
}

@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel,
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> signInWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            print(signInWithGoogleResponse.e)
            Log.d("signin", signInWithGoogleResponse.e.toString())
        }
    }
}