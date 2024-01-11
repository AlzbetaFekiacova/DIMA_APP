package eu.mobcomputing.dima.registration.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import eu.mobcomputing.dima.registration.uiEvents.RegistrationUIEvent
import eu.mobcomputing.dima.registration.uiStates.RegistrationUIState
import eu.mobcomputing.dima.registration.data.rules.Validator
import eu.mobcomputing.dima.registration.models.User
import eu.mobcomputing.dima.registration.navigation.Screen

/**
 * ViewModel responsible for handling registration-related logic and managing the UI state.
 */
class RegistrationViewModel : ViewModel() {
    private val TAG = RegistrationViewModel::class.simpleName

    // Represents the current state of the registration user interface
    var registrationUIState = mutableStateOf(RegistrationUIState())

    // Indicates whether all validation checks have passed
    var allValidationPassed = mutableStateOf(false)

    // Indicates whether the registration process is in progress
    var registrationInProgress = mutableStateOf(false)

    /**
     * Handles UI events triggered by user interactions.
     *
     * @param event The UI event to be processed.
     * @param navController The NavController for navigation purposes.
     */
    fun onEvent(event: RegistrationUIEvent, navController: NavController) {

        when (event) {
            is RegistrationUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
            }

            is RegistrationUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )

            }

            is RegistrationUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }

            is RegistrationUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }

            is RegistrationUIEvent.RegistrationButtonClicked -> {
                register(navController = navController)
            }
        }
        validateDataWithRules()
    }

    /**
     * Validates user input data based on predefined rules and updates the UI state accordingly.
     */
    private fun validateDataWithRules() {
        val firstNameValidation = Validator.validateFirstName(
            registrationUIState.value.firstName
        )
        val lastNameValidation = Validator.validateLastName(
            registrationUIState.value.lastName
        )
        val emailValidation = Validator.validateEmail(
            registrationUIState.value.email
        )
        val passwordValidation = Validator.validatePassword(
            registrationUIState.value.password
        )

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = firstNameValidation.status,
            lastNameError = lastNameValidation.status,
            emailError = emailValidation.status,
            passwordError = passwordValidation.status
        )

        allValidationPassed.value = firstNameValidation.status &&
                lastNameValidation.status &&
                emailValidation.status &&
                passwordValidation.status

    }

    /**
     * Initiates the registration process by creating a Firebase user account.
     *
     * @param navController The NavController for navigation purposes.
     */
    private fun register(navController: NavController) {
        createFirebaseUser(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            navController = navController
        )
    }

    /**
     * Creates a user document in Firestore database after successful Firebase user creation.
     *
     * @param email User's email address.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param userID Unique identifier for the user.
     * @param navController The NavController for navigation purposes.
     */
    private fun createUserInDatabase(
        email: String,
        firstName: String,
        lastName: String,
        userID: String,
        navController: NavController
    ) {
        val db = Firebase.firestore
        val user = User(
            firstName, lastName, email
        )

        val userDocumentRef: DocumentReference = db.collection("users").document(userID)

        userDocumentRef.set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User document created successfully")
                navController.navigate(
                    Screen.SignUnSuccessful.route,
                    builder = {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                )


            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error creating user document", e)
            }


    }


    /**
     * Creates a Firebase user account using email and password.
     *
     * @param email User's email address.
     * @param password User's chosen password.
     * @param navController The NavController for navigation purposes.
     */
    private fun createFirebaseUser(email: String, password: String, navController: NavController) {
        registrationInProgress.value = true
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside on Complete Lister")
                Log.d(TAG, "is Successful = ${it.isSuccessful}")
                if (it.isSuccessful) {
                    val userID = auth.currentUser?.uid
                    if (userID != null) {
                        createUserInDatabase(
                            firstName = registrationUIState.value.firstName,
                            lastName = registrationUIState.value.lastName,
                            email = email,
                            userID = userID,
                            navController = navController
                        )
                    }
                }

            }
            .addOnFailureListener {
                registrationInProgress.value = false
                Log.d(TAG, "Inside on Failure Lister")
                Log.d(TAG, "Exception = ${it.message}")

            }

    }

    /**
     * Navigates to the login screen using the provided NavController.
     *
     * @param navController The NavController for navigation purposes.
     */
    fun redirectToLogInScreen(navController: NavController) {
        navController.navigate(route = Screen.LogIn.route)
    }
}