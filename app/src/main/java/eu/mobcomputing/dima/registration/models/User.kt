package eu.mobcomputing.dima.registration.models

// Data class representing a user.
data class User(
    var uID :String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var dietType: DietType = DietType.OMNIVORE,
    var allergies: List<String> = emptyList(),

    var ingredientsInPantry : List<Ingredient> = emptyList(),
)