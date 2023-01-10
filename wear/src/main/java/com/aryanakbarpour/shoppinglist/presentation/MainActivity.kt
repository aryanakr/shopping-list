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
import com.aryanakbarpour.shoppinglist.presentation.screens.login.LoginScreen
import com.aryanakbarpour.shoppinglist.presentation.theme.ShoppingListTheme
import com.aryanakbarpour.shoppinglist.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val authViewModel : AuthViewModel by viewModels()


        setContent {
            ShoppingListTheme {

                LoginScreen(viewModel = authViewModel, navigateToShoppingList = {
                    navigateToShoppingList()
                }) {
                    launchGoogleSignIn()
                }
            }
        }
    }

    private fun navigateToShoppingList() {
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
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
                                navigateToShoppingList()
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