package com.aryanakbarpour.shoppinglist.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnitType.Companion.Em
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.R
import com.aryanakbarpour.shoppinglist.core.model.Response
import com.aryanakbarpour.shoppinglist.core.model.Response.*
import com.aryanakbarpour.shoppinglist.ui.components.AnimatedSurface
import com.aryanakbarpour.shoppinglist.ui.screens.Screen
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
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

    val errorState = remember {
        mutableStateOf<String?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val signInResult = remember { mutableStateOf<Response<Boolean>?>(null) }

    if (signInResult.value is Success) {
        signInResult.value = null
        navigateToShoppingList()
    }

    if (signInResult.value is Failure) {
        errorState.value = (signInResult.value as Failure).e.message
    }else {
        errorState.value = null
    }


    Scaffold() { padding ->
        AnimatedSurface(
            padding = padding,
        ) {

            Box(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(modifier = Modifier
                    .fillMaxWidth()) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        // Main card header
                        Text(
                            text = "LOGIN",
                            modifier = Modifier.padding(8.dp),
                            color = PrimaryDark,
                            style = MaterialTheme.typography.h5,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // login with email section
                        EmailLoginCard(
                            emailTextState = emailTextState,
                            passwordTextState = passwordTextState,
                            errorState = errorState,
                            signInResult = signInResult,
                            onLoginClicked = {
                                signInResult.value = Loading
                                coroutineScope.launch {
                                    signInResult.value = viewModel.loginWithEmail(
                                        emailTextState.value.text,
                                        passwordTextState.value.text
                                    )
                                }
                            },
                            onRegisterClicked = { navController.navigate(Screen.SignUpScreen.route) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // alternative login section
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            GoogleSignInComponent(viewModel, {i -> launch(i)} , navigateToShoppingList)

                            Spacer(modifier = Modifier.height(16.dp))

                            // offline login button
                            OutlinedButton(
                                onClick = {navigateToShoppingList()},
                                modifier = Modifier
                                    .width(275.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = "Continue Offline",
                                    modifier = Modifier.padding(6.dp),
                                    fontSize = 18.sp
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
