package eu.mobcomputing.dima.registration.data.registration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf

data class RegistrationUIState (
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",

    var firstNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false
)