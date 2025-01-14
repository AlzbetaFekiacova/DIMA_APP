package eu.mobcomputing.dima.registration.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.mobcomputing.dima.registration.R
import eu.mobcomputing.dima.registration.components.HeaderTextComponent
import eu.mobcomputing.dima.registration.components.MyAlertDialog
import eu.mobcomputing.dima.registration.components.NavigationBarComponent
import eu.mobcomputing.dima.registration.components.SearchBar
import eu.mobcomputing.dima.registration.components.add_ingredients.IngredientVerticalGrid
import eu.mobcomputing.dima.registration.viewmodels.SearchIngredientViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    viewModel: SearchIngredientViewModel = hiltViewModel()
) {


    val isNetworkAvailable = viewModel.connectionStatus.observeAsState()

    // Observe the LiveData containing the list of Ingredients
    val ingredientsList = viewModel.ingredients.observeAsState()


    // State for holding the search text
    var searchText by remember { mutableStateOf("") }



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        bottomBar = {
            NavigationBarComponent(
                navController = navController,
                selectedItemIndex = 3
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(colorResource(id = R.color.pink_50)),
        ) {

            SearchBar(
                onSearch = { newSearchText ->
                    searchText = newSearchText
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.searchIngredient(newSearchText)
                    }


                },
                onSearchTextChange = { newSearchText ->
                    searchText = newSearchText
                    // Call the filter function in the ViewModel
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.searchIngredient(newSearchText)
                    }
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                ), thickness = 1.dp, color = Color.LightGray
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(weight = 1f, fill = true)
            ) {
                ingredientsList.value?.let {
                    if (it.isNotEmpty()) {
                        //populate grid if ingredients found
                        IngredientVerticalGrid(ingredients = it, navController, viewModel)
                    } else {

                        // Ingredients not found
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            HeaderTextComponent(
                                value = stringResource(R.string.search_ingredient_text)
                            )
                        }
                    }
                }
            }
        }

        if (isNetworkAvailable.value == false) {
            viewModel.openAlertDialog.value = true
            val context = LocalContext.current
            MyAlertDialog(
                onDismissRequest = {
                    viewModel.openAlertDialog.value = false
                },
                onConfirmation = { (context as Activity).finishAffinity() },
                dialogTitle = stringResource(id = R.string.connection_not_available),
                dialogText = stringResource(id = R.string.i_need_conection_txt),
                icon = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                confirmationText = stringResource(id = R.string.ok),
                dismissText = ""
            )
        }

    }
}


@Preview
@Composable
fun SearchIngredientScreenPreview() {
    SearchIngredientScreen(navController = rememberNavController())
}