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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eu.mobcomputing.dima.registration.R
import eu.mobcomputing.dima.registration.components.ButtonComponent
import eu.mobcomputing.dima.registration.components.ClickableRegisterTextComponent
import eu.mobcomputing.dima.registration.components.HeaderTextComponent
import eu.mobcomputing.dima.registration.components.MyAlertDialog
import eu.mobcomputing.dima.registration.components.MyImageComponent
import eu.mobcomputing.dima.registration.components.MyPasswordFieldComponent
import eu.mobcomputing.dima.registration.components.MyTextFieldComponent
import eu.mobcomputing.dima.registration.components.NormalTextComponent
import eu.mobcomputing.dima.registration.uiEvents.RegistrationUIEvent
import eu.mobcomputing.dima.registration.viewmodels.RegistrationViewModel

/**
 * Composable function representing the SignUp screen of the application.
 *
 * @param navController NavController for navigating between screens.
 * @param registrationViewModel ViewModel managing the logic for the LogIn screen.
 */
@Composable
fun SignUpScreen(
    navController: NavController, registrationViewModel: RegistrationViewModel = viewModel(),
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colorResource(id = R.color.pink_50)
            ),

        contentAlignment = Alignment.Center
    ) {
        val isSmallScreen = maxWidth < 600.dp
        if (isSmallScreen) {
            SmallRegistrationScreen(
                navController = navController,
                registrationViewModel = registrationViewModel,
            )
        }
        // If the screen is wider, if it is a tablet
        else {
            WideRegistrationScreen(
                navController = navController,
                registrationViewModel = registrationViewModel,

                )
        }
    }
}

/**
 * Composable function representing the SignUp screen of the application for small screen (mobile).
 *
 * @param navController NavController for navigating between screens.
 * @param registrationViewModel ViewModel managing the logic for the LogIn screen.
 */
@Composable
fun SmallRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        HeaderTextComponent(
            value = stringResource(id = R.string.create_account_text),
            shouldBeCentered = true,
            shouldBeRed = true
        )
        Spacer(modifier = Modifier.height(10.dp))
        HeaderTextComponent(value = stringResource(id = R.string.personal_information))
        Spacer(modifier = Modifier.height(10.dp))

        // First name Component
        MyTextFieldComponent(
            labelValue = stringResource(id = R.string.first_name),
            leadingIcon = painterResource(
                id = R.drawable.baseline_person_24
            ),
            onTextSelected = {
                registrationViewModel.onEvent(
                    RegistrationUIEvent.FirstNameChanged(it), navController
                )
            },
            errorStatus = registrationViewModel.registrationUIState.value.firstNameError,

            )

        // Last name component
        MyTextFieldComponent(
            labelValue = stringResource(id = R.string.last_name),
            leadingIcon = painterResource(
                id = R.drawable.baseline_person_24
            ),
            onTextSelected = {
                registrationViewModel.onEvent(
                    RegistrationUIEvent.LastNameChanged(it), navController,

                    )
            },
            errorStatus = registrationViewModel.registrationUIState.value.lastNameError,
        )

        //R-mail component
        MyTextFieldComponent(
            labelValue = stringResource(id = R.string.email),
            leadingIcon = painterResource(
                id = R.drawable.baseline_alternate_email_24
            ),
            onTextSelected = {
                registrationViewModel.onEvent(
                    RegistrationUIEvent.EmailChanged(it), navController,

                    )
            },
            errorStatus = registrationViewModel.registrationUIState.value.emailError,
        )

        // Password component
        MyPasswordFieldComponent(
            labelValue = stringResource(id = R.string.password),
            leadingIcon = painterResource(
                id = R.drawable.baseline_lock_24
            ),
            onTextSelected = {
                registrationViewModel.onEvent(
                    RegistrationUIEvent.PasswordChanged(it), navController,

                    )
            },
            errorStatus = registrationViewModel.registrationUIState.value.passwordError,
            supportingText = "The password must contain 8 characters, 1 upper case letter and 3 numbers."
        )


        Spacer(modifier = Modifier.weight(1f))
        ButtonComponent(value = stringResource(id = R.string.sign_up),
            isEnabled = registrationViewModel.allValidationPassed.value,
            onClickAction = {
                registrationViewModel.onEvent(
                    RegistrationUIEvent.RegistrationButtonClicked, navController,
                )
            })
        Spacer(modifier = Modifier.height(10.dp))
        NormalTextComponent(value = stringResource(id = R.string.or))
        ClickableRegisterTextComponent(onClickAction = {
            registrationViewModel.redirectToLogInScreen(navController)
        })
    }



    if (registrationViewModel.registrationInProgress.value) {
        CircularProgressIndicator()
    }
    if (registrationViewModel.openAlertDialog.value) {
        MyAlertDialog(
            onDismissRequest = { registrationViewModel.openAlertDialog.value = false },
            onConfirmation = { registrationViewModel.redirectToLogInScreen(navController) },
            dialogTitle = stringResource(R.string.e_mail_already_registered),
            dialogText = "I am sorry, the e-mail you are trying to register, I already know. " +
                    "Please provide different e-mail or log in via provided e-mail. " +
                    "If you forgot your password, please click on Yes, I will redirect you to log in page, where you can " +
                    "reset your password.",
            icon = Icons.Default.Info,
            confirmationText = "Yes, go to log in",
            dismissText = "No, create new account",
        )
    }

}

/**
 * Composable function representing the SignUp screen of the application for wide screen (tablet).
 *
 * @param navController NavController for navigating between screens.
 * @param registrationViewModel ViewModel managing the logic for the LogIn screen.
 */
@Composable
fun WideRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel,
) {
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
                R.drawable.sous_chef, modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            HeaderTextComponent(
                value = stringResource(id = R.string.create_account_text),
                shouldBeCentered = true,
                shouldBeRed = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            HeaderTextComponent(value = stringResource(id = R.string.personal_information))
            Spacer(modifier = Modifier.height(10.dp))

            // First name Component
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.first_name),
                leadingIcon = painterResource(
                    id = R.drawable.baseline_person_24
                ),
                onTextSelected = {
                    registrationViewModel.onEvent(
                        RegistrationUIEvent.FirstNameChanged(it), navController,
                    )
                },
                errorStatus = registrationViewModel.registrationUIState.value.firstNameError,

                )

            // Last name component
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.last_name),
                leadingIcon = painterResource(
                    id = R.drawable.baseline_person_24
                ),
                onTextSelected = {
                    registrationViewModel.onEvent(
                        RegistrationUIEvent.LastNameChanged(it), navController,
                    )
                },
                errorStatus = registrationViewModel.registrationUIState.value.lastNameError,
            )
            //R-mail component
            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                leadingIcon = painterResource(
                    id = R.drawable.baseline_alternate_email_24
                ),
                onTextSelected = {
                    registrationViewModel.onEvent(
                        RegistrationUIEvent.EmailChanged(it), navController,
                    )
                },
                errorStatus = registrationViewModel.registrationUIState.value.emailError,
            )

            // Password component
            MyPasswordFieldComponent(
                labelValue = stringResource(id = R.string.password),
                leadingIcon = painterResource(
                    id = R.drawable.baseline_lock_24
                ),
                onTextSelected = {
                    registrationViewModel.onEvent(
                        RegistrationUIEvent.PasswordChanged(it), navController,
                    )
                },
                errorStatus = registrationViewModel.registrationUIState.value.passwordError,
                supportingText = "The password must contain 8 characters, 1 upper case letter and 3 numbers."

            )

            ButtonComponent(value = stringResource(id = R.string.sign_up),
                isEnabled = registrationViewModel.allValidationPassed.value,
                onClickAction = {
                    registrationViewModel.onEvent(
                        RegistrationUIEvent.RegistrationButtonClicked, navController,
                    )
                })
            NormalTextComponent(value = stringResource(id = R.string.or))
            ClickableRegisterTextComponent(onClickAction = {
                registrationViewModel.redirectToLogInScreen(navController)
            })
        }

        if (registrationViewModel.registrationInProgress.value) {
            CircularProgressIndicator()
        }
        if (registrationViewModel.openAlertDialog.value) {
            MyAlertDialog(
                onDismissRequest = { registrationViewModel.openAlertDialog.value = false },
                onConfirmation = { registrationViewModel.redirectToLogInScreen(navController) },
                dialogTitle = stringResource(R.string.e_mail_already_registered),
                dialogText = "I am sorry, the e-mail you are trying to register, I already know. " +
                        "Please provide different e-mail or log in via provided e-mail. " +
                        "If you forgot your password, please click on Yes, I will redirect you to log in page, where you can " +
                        "reset your password.",
                icon = Icons.Default.Info,
                confirmationText = "Yes, go to log in",
                dismissText = "No, create new account",
            )
        }
    }
}


/**
 * Preview annotation for previewing the SignUpScreen in Android Studio.
 */
@Preview(device = Devices.PIXEL_TABLET)
@Preview(device = Devices.PIXEL_6)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
