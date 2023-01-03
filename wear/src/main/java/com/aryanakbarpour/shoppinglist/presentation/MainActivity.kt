/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.aryanakbarpour.shoppinglist.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.aryanakbarpour.shoppinglist.R
import com.aryanakbarpour.shoppinglist.presentation.screens.AllListsScreen
import com.aryanakbarpour.shoppinglist.presentation.screens.LoginScreen
import com.aryanakbarpour.shoppinglist.presentation.screens.Screen
import com.aryanakbarpour.shoppinglist.presentation.screens.ViewListScreen
import com.aryanakbarpour.shoppinglist.presentation.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModel
import com.aryanakbarpour.shoppinglist.viewmodel.UserViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleApiClient: GoogleApiClient

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val authViewModel : AuthViewModel by viewModels()
        val userViewModel : UserViewModel by viewModels()
        val shoppingListViewModel : ShoppingListViewModel by viewModels()

        setContent {
            ShoppingListTheme {
                navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
                    composable(Screen.LoginScreen.route) {
                        LoginScreen(authViewModel, {navController.navigate(Screen.AllListsScreen.route)}, {launchGoogleSignIn()})
                    }

                    composable(Screen.AllListsScreen.route) {
                        AllListsScreen(navController, userViewModel, shoppingListViewModel)

                    }

                    composable(
                        route = Screen.ViewListScreen.route + "/{listId}",
                        arguments = listOf(navArgument("listId") {
                            type = NavType.StringType
                            nullable = false
                        })
                    ) { entry ->
                        ViewListScreen(navController = navController, viewModel= shoppingListViewModel, listId = entry.arguments?.getString("listId")!!)
                    }
                }
            }
        }
    }

    fun launchGoogleSignIn() {
        Auth.GoogleSignInApi.getSignInIntent(googleApiClient).also { signInIntent ->
            startActivityForResult(signInIntent, 8585)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == 8585) {
            if (data != null) {
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)?.apply {
                    if (isSuccess) {
                        Log.d("MainActivity", "Google Sign In Success ${signInAccount?.email}")
                        signInAccount?.idToken?.let { token ->
                            FirebaseAuth.getInstance().signInWithCredential(
                                com.google.firebase.auth.GoogleAuthProvider.getCredential(token, null)
                            )
                            .addOnSuccessListener {
                                navController.navigate(Screen.AllListsScreen.route)
                            }
                            .addOnFailureListener {
                                Log.d("MainActivity", "Firebase Sign In Failed ${it.message}")
                            }
                        }

                    }
                }
            }
        }
    }

}

@Composable
fun WearApp(greetingName: String) {
    ShoppingListTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}
