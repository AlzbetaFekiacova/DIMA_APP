package eu.mobcomputing.dima.registration.navigation


/**
 * Sealed class representing screens.
 */
sealed class Screen(val route: String) {
    data object Welcome : Screen(route = "welcome_screen")
    data object Register : Screen(route = "register_screen")
    data object LogIn : Screen(route = "login_screen")
    data object Home : Screen(route = "home_screen")
    data object UserAllergies : Screen(route = "user_allergies_screen")
    data object UserDiet : Screen(route = "user_diet_screen")
    data object SignUnSuccessful : Screen(route = "sign_up_successful_screen")
    data object Pantry : Screen(route = "pantry_screen")
    data object Profile : Screen(route = "profile_screen")
    data object Search : Screen(route = "search_screen")
    data object SearchIngredientScreen: Screen(route="search_ingredients")
    data object AddIngredientToFridgeScreen : Screen(route = "addToFridge/{ingredient}")
    data object SplashScreen: Screen(route = "splash_screen")
    data object RecipeDetail: Screen(route = "recipeDetail/{recipe}")

    data object NoInternetConnection: Screen(route = "no_internet_connection_screen")
}