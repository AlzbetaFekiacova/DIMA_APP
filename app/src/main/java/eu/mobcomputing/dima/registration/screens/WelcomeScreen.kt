package eu.mobcomputing.dima.registration.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.mobcomputing.dima.registration.R
import eu.mobcomputing.dima.registration.components.ButtonComponent
import eu.mobcomputing.dima.registration.components.MyImageComponent
import eu.mobcomputing.dima.registration.viewmodels.WelcomeViewModel

/**
 * Composable function representing the Welcome screen of the application.
 *
 * @param navController NavController for navigating between screens.
 */
@Composable
fun WelcomeScreen(navController: NavController, welcomeViewModel: WelcomeViewModel = viewModel()) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.pink_50)),
        contentAlignment = Alignment.Center
    ) {
        val isSmallScreen = maxWidth < 600.dp

        if (isSmallScreen) {
            SmallWelcomeScreenView(navController, welcomeViewModel)
        }
        // If the screen is wider, if it is a tablet
        else {
            WideWelcomeScreenView(navController = navController, welcomeViewModel)
        }
    }
}

/**
 * Composable function representing the Welcome screen of the application for smaller screens (mobile).
 *
 * @param navController NavController for navigating between screens.
 * @param welcomeViewModel ViewModel of the Welcome screen to support the logic behind the screen.
 */
@Composable
fun SmallWelcomeScreenView(navController: NavController, welcomeViewModel: WelcomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            MyImageComponent(
                R.drawable.sous_chef,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ButtonComponent(
            value = stringResource(id = R.string.sign_up_log_in),
            onClickAction = { welcomeViewModel.redirection(navController) },
            isEnabled = true
        )
    }
}

/**
 * Composable function representing the Welcome screen of the application for wider screens (tablet).
 *
 * @param navController NavController for navigating between screens.
 * @param welcomeViewModel ViewModel of the Welcome screen to support the logic behind the screen.
 */
@Composable
fun WideWelcomeScreenView(navController: NavController, welcomeViewModel: WelcomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            MyImageComponent(
                R.drawable.sous_chef,
                modifier = Modifier.fillMaxSize()
            )
        }

        ButtonComponent(
            value = stringResource(id = R.string.sign_up_log_in),
            onClickAction = { welcomeViewModel.redirection(navController) },
            isEnabled = true
        )


    }
}

/**
 * Preview annotation for previewing the WelcomeScreen in Android Studio.
 */
@Preview(device = Devices.PIXEL_TABLET)
@Preview(device = Devices.PIXEL_6)
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreen(navController = rememberNavController())
}

