package eu.mobcomputing.dima.registration.data.registration

sealed class RegistrationUIEvent {
    data class FirstNameChanged(val firstName: String): RegistrationUIEvent()
    data class LastNameChanged(val lastName: String): RegistrationUIEvent()
    data class EmailChanged(val email: String): RegistrationUIEvent()
    data class PasswordChanged(val password: String): RegistrationUIEvent()
    data object RegistrationButtonClicked: RegistrationUIEvent()
    //data object NextButtonClicked: RegistrationUIEvent()

}