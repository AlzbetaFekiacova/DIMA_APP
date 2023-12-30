package eu.mobcomputing.dima.registration.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.mobcomputing.dima.registration.R
import eu.mobcomputing.dima.registration.components.ButtonComponent
import eu.mobcomputing.dima.registration.components.DatePickerView
import eu.mobcomputing.dima.registration.components.HeaderTextComponent
import eu.mobcomputing.dima.registration.components.QuantitySelector
import eu.mobcomputing.dima.registration.models.Ingredient
import eu.mobcomputing.dima.registration.navigation.Screen

@Composable
fun AddIngredientToFridge(
    navController: NavController,
    ingredient: Ingredient,
    //homeViewModel: HomeViewModel = viewModel()
) {

    var date by remember {
        mutableStateOf("Open date picker dialog")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }



    Surface(
        color = colorResource(id = R.color.pink_50),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.pink_50))
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            //verticalArrangement = Arrangement.SpaceBetween
        ){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.SearchIngredientScreen.route) {
                        launchSingleTop = true
                        }
                    }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly

            ){


                HeaderTextComponent(
                    value =  ingredient.name
                )


                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ){

                    Text(
                        text = "Select the quantity: ",
                        style =  TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    )


                    QuantitySelector(
                        onQuantityChange = {},
                        modifier = Modifier.weight(1f)
                        )

                }



                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Add the expiring date: ",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    )


                    Button(
                        onClick = { showDatePicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.pink_900)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = date,
                            fontSize = 15.sp,
                            color = colorResource(id = R.color.pink_100),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        )
                    }


                    if (showDatePicker) {
                        DatePickerView(
                            onDismiss = { showDatePicker = false },
                            onDateSelected = { date = it },
                        )
                    }
                }


            }

            ButtonComponent(
                value = "Add to pantry",
                onClickAction = {},
                isEnabled = true)

        }


    }
}


@Preview
@Composable
fun AddIngredientToFridgePreview() {
    val ingredient = Ingredient(
        id= 1,
        name = "Egg",
        image = "",
        )
    AddIngredientToFridge(navController = rememberNavController(),ingredient= ingredient)
}