package eu.mobcomputing.dima.registration.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobcomputing.dima.registration.DialogManager
import eu.mobcomputing.dima.registration.FirebaseAuthManager
import eu.mobcomputing.dima.registration.Result
import eu.mobcomputing.dima.registration.data.rules.Validator
import eu.mobcomputing.dima.registration.models.User
import eu.mobcomputing.dima.registration.navigation.Screen
import eu.mobcomputing.dima.registration.uiEvents.RegistrationUIEvent
import eu.mobcomputing.dima.registration.uiStates.RegistrationUIState
import kotlinx.coroutines.launch
import javax.inject.Inject

/*class SignUpViewModel(val userRepository: UserRepository) : ViewModel(), CoroutineScope {

    // set coroutine context
    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    // -- Coroutine jobs
    private var getUserJob: Job? = null
    private var createUserJob: Job? = null

    // -- Live data
    private val _snackbarText = MutableLiveData<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarText

    private val _userLD = MutableLiveData<User>()
    val userLD: LiveData<User> = _userLD

    // get a user infromation from Firestore with its id
    fun fetchUserInformationFromFirestore(userID: String) {
        if (getUserJob?.isActive == true) getUserJob?.cancel()
        getUserJob = launch {
            when (val result = userRepository.getUserFromFirestore(userID)) {
                is Result.Success -> _userLD.value = result.data!!
                is Result.Error -> _snackbarText.value = R.string.error_fetching
                is Result.Canceled -> _snackbarText.value = R.string.canceled
                else -> {}
            }
        }
    }

    // create a user in firestore
    fun createUserToFirestore(firstName: String, lastName:String, email: String){
        val user = User(firstName = firstName, lastName = lastName, email = email)
        if(createUserJob?.isActive == true) createUserJob?.cancel()
        createUserJob = launch {
            when(userRepository.createUserInFirestore(user)){
                is Result.Success -> _snackbarText.value = R.string.user_created
                is Result.Error -> _snackbarText.value = R.string.error_creating
                is Result.Canceled -> _snackbarText.value = R.string.canceled
            }
        }
    }
}*/
@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val firebaseAuthManager: FirebaseAuthManager,
    private val dialogManager: DialogManager
) : AndroidViewModel(application) {
    private val TAG = RegistrationViewModel::class.simpleName

    // Represents the current state of the registration user interface
    var registrationUIState = mutableStateOf(RegistrationUIState())

    // Indicates whether all validation checks have passed
    var allValidationPassed = mutableStateOf(false)

    // Indicates whether the registration process is in progress
    var registrationInProgress = mutableStateOf(false)

    // Indicates whether the registration has been successful
    // 0 -> successful
    // 1 -> e-mail address already used
    // 2 -> other issue
    private var registrationSuccessful = mutableIntStateOf(0)
    // ... existing code ...

    fun createFirebaseUser(
        email: String,
        password: String,
        navController: NavController,
        context: Context
    ) {
        viewModelScope.launch {
            registrationInProgress.value = true
            val result = firebaseAuthManager.createUserWithEmailAndPassword(email, password)
            handleFirebaseResult(result, email, navController, context)
        }
    }

    private fun handleFirebaseResult(
        result: Result<String>,
        email: String,
        navController: NavController,
        context: Context
    ) {
        when (result) {
            is Result.Success -> {
                val userID = result.data
                registrationSuccessful.intValue = 0
                createUserInDatabase(
                    firstName = registrationUIState.value.firstName,
                    lastName = registrationUIState.value.lastName,
                    email = email,
                    userID = userID,
                    navController = navController
                )
            }

            is Result.Error -> {
                registrationInProgress.value = false
                handleFirebaseError(result.exception, navController, context)
            }
            // Handle other Result types as needed
            else -> {}
        }
    }

    private fun handleFirebaseError(
        exception: Exception,
        navController: NavController,
        context: Context
    ) {
        // Handle Firebase errors (e.g., show alert dialog)
        // You can also create an interface for dialog operations to make testing easier
        // For simplicity, I'll directly call the existing method in the ViewModel
        if (exception is FirebaseAuthUserCollisionException) {
            // Handle email collision
            dialogManager.showEmailAlreadyRegisteredDialog(context, navController)
        } else {
            // Handle other errors
            registrationSuccessful.intValue = 2
            Log.d(TAG, "Inside on Failure Lister")
            Log.d(TAG, "Exception = ${exception.message}")
        }
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
     * Handles UI events triggered by user interactions.
     *
     * @param event The UI event to be processed.
     * @param navController The NavController for navigation purposes.
     */
    fun onEvent(event: RegistrationUIEvent, navController: NavController, context: Context) {

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
                createFirebaseUser(
                    context = context,
                    email = registrationUIState.value.email,
                    password = registrationUIState.value.password,
                    navController = navController
                )
            }
        }
        validateDataWithRules()
    }

    /**
     * Validates user input data based on predefined rules and updates the UI state accordingly.
     */
    fun validateDataWithRules() {
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
}
