package eu.mobcomputing.dima.registration

interface FirebaseAuthManager {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String>
    // Add other Firebase operations as needed
}