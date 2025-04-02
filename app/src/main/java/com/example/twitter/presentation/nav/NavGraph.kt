package com.example.twitter.presentation.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twitter.presentation.auth.AuthScreen
import com.example.twitter.presentation.auth.Login
import com.example.twitter.presentation.auth.Splash
import com.example.twitter.presentation.auth.WelcomeScreen
import com.example.twitter.presentation.main.callScreen.CallScreen
import com.example.twitter.presentation.main.chat.ChatScreen
import com.example.twitter.presentation.main.communities.Communities
import com.example.twitter.presentation.main.home.HomeScreen
import com.example.twitter.presentation.main.setting.SettingScreen
import com.example.twitter.presentation.main.update.UpdateScr
import com.example.twitter.presentation.main.userProfile.ProfileScreen
import com.example.twitter.presentation.main.userProfile.SetUserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.serialization.Serializable

@Composable
fun NavGraphs(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    val currentUser = Firebase.auth.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate(Routes.HomeScreen) {
                popUpTo(Routes.WelcomeScreen) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.WelcomeScreen)
        }
    }

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable<Routes.Splash> { Splash(navController) }
        composable<Routes.Login> { Login() }
        composable<Routes.Communities> { Communities(navController = navController) }
        composable<Routes.HomeScreen> {
            HomeScreen(navController = navController, baseViewModel = hiltViewModel())
        }
        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController, innerPadding)
        }
        composable<Routes.AuthScreen> {
            AuthScreen(navController = navController, innerPadding = innerPadding)
        }
        composable<Routes.ProfileScreen> {
            ProfileScreen(innerPadding, navController)
        }
        composable<Routes.CallScreen> {
            CallScreen(navController, innerPadding)
        }
        composable<Routes.UpdateScreen> {
            UpdateScr(navController)
        }
        composable<Routes.SettingsScreen> {
            SettingScreen(innerPadding, navController)
        }
        composable<Routes.SetUserProfile> {
            SetUserProfile(innerPadding, navController)
        }
        composable<Routes.ChatScreen> {
            val chatId = it.arguments?.getString("chatId")
            if (chatId != null) {
                ChatScreen(navController = navController, chatId = chatId)
            }
        }
    }
}

@Serializable
sealed class Routes {
    @Serializable
    data object Splash : Routes()
    @Serializable
    data object Login : Routes()
    @Serializable
    data object Communities : Routes()
    @Serializable
    data object HomeScreen : Routes()
    @Serializable
    data object UpdateScreen : Routes()
    @Serializable
    data object WelcomeScreen : Routes()
    @Serializable
    data object AuthScreen : Routes()
    @Serializable
    data object ProfileScreen : Routes()
    @Serializable
    data object CallScreen : Routes()
    @Serializable
    data class ChatScreen(val chatId: String) : Routes()
    @Serializable
    data object SettingsScreen : Routes()
    @Serializable
    data object SetUserProfile: Routes()

}
