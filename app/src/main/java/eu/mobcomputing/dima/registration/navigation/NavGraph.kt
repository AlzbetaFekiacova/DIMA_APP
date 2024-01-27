package eu.mobcomputing.dima.registration.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import eu.mobcomputing.dima.registration.models.Ingredient
import eu.mobcomputing.dima.registration.models.Recipe
import eu.mobcomputing.dima.registration.screens.AddIngredientToPantry
import eu.mobcomputing.dima.registration.screens.HomeScreen
import eu.mobcomputing.dima.registration.screens.LogInScreen
import eu.mobcomputing.dima.registration.screens.NoInternetConnectionScreen
import eu.mobcomputing.dima.registration.screens.PantryScreen
import eu.mobcomputing.dima.registration.screens.ProfileScreen
import eu.mobcomputing.dima.registration.screens.RecipeDetailScreen
import eu.mobcomputing.dima.registration.screens.SearchIngredientScreen
import eu.mobcomputing.dima.registration.screens.SignUnSuccessfulScreen
import eu.mobcomputing.dima.registration.screens.SignUpScreen
import eu.mobcomputing.dima.registration.screens.SplashScreen
import eu.mobcomputing.dima.registration.screens.UserAllergiesScreen
import eu.mobcomputing.dima.registration.screens.UserDietScreen
import eu.mobcomputing.dima.registration.screens.WelcomeScreen
import eu.mobcomputing.dima.registration.viewmodels.SharedUserDataModelViewFactory
import eu.mobcomputing.dima.registration.viewmodels.SharedUserDataViewModel
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SetUpNavGraph(navController: NavHostController) {

    // Create a shared instance of SharedUserDataModel using the SharedUserDataModelViewFactory
    val sharedViewModelFactory = SharedUserDataModelViewFactory()
    val sharedViewModel: SharedUserDataViewModel = viewModel(factory = sharedViewModelFactory)


    // NavHost composable defines the navigation structure
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    )
    {
        // composable functions for different screens
        // Splash Screen
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        // Welcome Screen
        composable(
            route = Screen.Welcome.route
        ) {
            WelcomeScreen(navController)
        }

        // Log in Screen
        composable(
            route = Screen.LogIn.route
        ) {
            LogInScreen(navController)
        }

        // Sign up Screen
        composable(
            route = Screen.Register.route
        ) {
            SignUpScreen(navController)
        }

        // Home Screen
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController)
        }

        // User Allergies Screen
        composable(route = Screen.UserAllergies.route) {
            UserAllergiesScreen(navController = navController, sharedViewModel)
        }

        // User Diet Screen
        composable(route = Screen.UserDiet.route) {
            UserDietScreen(navController = navController, sharedViewModel)
        }

        // Successful signing in Screen
        composable(route = Screen.SignUnSuccessful.route) {
            SignUnSuccessfulScreen(navController = navController)
        }

        // Pantry Screen
        composable(route = Screen.Pantry.route) {
            PantryScreen(navController = navController)
        }

        // Profile Screen
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(route = Screen.SearchIngredientScreen.route) {
            SearchIngredientScreen(navController = navController)
        }

        composable(route = Screen.AddIngredientToFridgeScreen.route) { backStackEntry ->
            // Extract the ingredientJSON from the route
            val ingredientJSON = backStackEntry.arguments?.getString("ingredient") ?: ""

            //convert it back into object
            val ingredient: Ingredient? = try {
                val decodedString = URLDecoder.decode(ingredientJSON, "utf-8")
                Gson().fromJson(decodedString, Ingredient::class.java)
            } catch (e: Exception) {
                null
            }

            // Display the details screen using the extracted obj
            ingredient?.let {
                AddIngredientToPantry(
                    navController = navController,
                    ingredient = it
                )
            }
        }


        composable(route = Screen.RecipeDetail.route) { backStackEntry ->
            // Extract the ingredientJSON from the route
            val recipeJSON = backStackEntry.arguments?.getString("recipe") ?: ""

            Log.e("###", recipeJSON)

            //convert it back into object
            val recipe: Recipe? = try {
                val decodedString = URLDecoder.decode(recipeJSON, "utf-8")
                Gson().fromJson(decodedString, Recipe::class.java)
            } catch (e: Exception) {
                null
            }

            Log.e("XXX",recipe.toString())

            // Display the details screen using the extracted obj
            recipe?.let {
                RecipeDetailScreen(
                    navController = navController,
                    recipe = it
                )
            }
        }

        composable(route = Screen.NoInternetConnection.route){
            NoInternetConnectionScreen(navController = navController)
        }
    }

}