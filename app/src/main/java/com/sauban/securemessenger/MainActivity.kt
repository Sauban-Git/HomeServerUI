package com.sauban.securemessenger

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sauban.securemessenger.helper.getToken
import com.sauban.securemessenger.network.ApiClient
import com.sauban.securemessenger.screens.ChatScreen
import com.sauban.securemessenger.screens.ConversationScreen
import com.sauban.securemessenger.screens.HomeScreen
import com.sauban.securemessenger.screens.SignupScreen
import com.sauban.securemessenger.ui.theme.SecureMessengerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureMessengerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "Splash", modifier = Modifier.fillMaxSize()) {
            composable("Home") {
                HomeScreen(navController = navController)
            }
            composable("Splash") {
                SplashScreen(navController)
            }
            composable("ConversationScreen") {
                ConversationScreen()
            }
            composable("SignupScreen") {
                SignupScreen(navController)
            }
        }
    }


}

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val token = getToken(context)

        if (token.isNullOrEmpty()) {
            navController.navigate("SignupScreen") {
                popUpTo("Splash") { inclusive = true }
            }
        } else {
            try {
                ApiClient.setToken(token)

                // Call protected endpoint to verify token
                ApiClient.apiService.getUserInfo()

                // If request succeeds → token valid
                navController.navigate("ConversationScreen") {
                    popUpTo("Splash") { inclusive = true }
                }

            } catch (e: Exception) {
                // Token invalid
                navController.navigate("SignupScreen") {
                    popUpTo("Splash") { inclusive = true }
                }
            }
        }
    }

    // Simple loading UI (replace with animation if you want)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
