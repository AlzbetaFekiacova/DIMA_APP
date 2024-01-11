package eu.mobcomputing.dima.registration.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.mobcomputing.dima.registration.R
import eu.mobcomputing.dima.registration.components.IngredientVerticalGrid
import eu.mobcomputing.dima.registration.components.SearchBar
import eu.mobcomputing.dima.registration.viewmodels.SearchIngredientViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.mobcomputing.dima.registration.components.HeaderTextComponent

/**
 * Composable function representing the screen for searching and displaying a list of ingredients.
 *
 * This screen utilizes a [SearchIngredientViewModel] to manage and observe the list of ingredients.
 *
 * @param navController The NavController for navigation within the application.
 * @param viewModel The [SearchIngredientViewModel] used for handling data and business logic.
 */
@Composable
fun SearchIngredientScreen(
    navController: NavController,
    viewModel: SearchIngredientViewModel = hiltViewModel() ) {

    // Observe the LiveData containing the list of Ingredients
    val ingredientsList = viewModel.ingredients.observeAsState()


    // State for holding the search text
    var searchText by remember { mutableStateOf("") }



    Surface(
        color = colorResource(id = R.color.pink_50),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.pink_50))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ){


            SearchBar(
                viewModel = viewModel,
                onSearchTextChange = { newSearchText ->
                    searchText = newSearchText
                    // Call the filter function in the ViewModel
                    viewModel.filterIngredients(newSearchText)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                ), thickness = 1.dp, color = Color.LightGray
            )

            ingredientsList.value?.let {
                if (it.isNotEmpty()){
                    //populate grid if ingredients found
                    IngredientVerticalGrid(ingredients = it,navController)
                }else {

                    // Ingredients not found
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        HeaderTextComponent(
                            value = "No ingredient found"
                        )
                    }

                }

            }
        }
    }
}



@Preview
@Composable
fun SearchIngredientScreenPreview() {
    SearchIngredientScreen(navController = rememberNavController())
}