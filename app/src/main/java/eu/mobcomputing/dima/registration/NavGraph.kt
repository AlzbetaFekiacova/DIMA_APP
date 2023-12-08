package eu.mobcomputing.dima.registration

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.mobcomputing.dima.registration.screens.LogInScreen
import eu.mobcomputing.dima.registration.screens.SignUpScreen
import eu.mobcomputing.dima.registration.screens.WelcomeScreen

@Composable
fun SetUpNavGraph( navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = Screen.Welcome.route)
    {
        composable(
            route = Screen.Welcome.route
        ){
            WelcomeScreen(navController)
        }

        composable(
            route = Screen.LogIn.route
        ){
            LogInScreen(navController)
        }

        composable(
            route = Screen.Register.route
        ){
            SignUpScreen(navController)
        }

    }
}