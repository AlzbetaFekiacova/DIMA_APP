package eu.mobcomputing.dima.registration


import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthManagerImpl : FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val TAG = FirebaseAuthManagerImpl::class.simpleName

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(result.user?.uid ?: "")
        } catch (exception: Exception) {
            handleFirebaseException(exception)
        }
    }

    private suspend fun <T> Task<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { result ->
                continuation.resume(result)
            }
            addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }

    private fun handleFirebaseException(exception: Exception): Result.Error {
        if (exception is FirebaseAuthUserCollisionException) {
            val errorCode = exception.errorCode
            Log.d(TAG, "Exception error code = $errorCode")
            if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                return Result.Error(exception)
            }
        } else {
            Log.d(TAG, "Inside on Failure Lister")
            Log.d(TAG, "Exception = ${exception.message}")
        }
        return Result.Error(exception)
    }
}
