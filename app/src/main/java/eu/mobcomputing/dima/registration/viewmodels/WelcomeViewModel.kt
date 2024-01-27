package eu.mobcomputing.dima.registration.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import eu.mobcomputing.dima.registration.ConnectivityObserver
import eu.mobcomputing.dima.registration.ContextManager
import eu.mobcomputing.dima.registration.NetworkConnectivityObserver
import eu.mobcomputing.dima.registration.navigation.Screen
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {
    private val TAG = WelcomeViewModel::class.simpleName
    @SuppressLint("StaticFieldLeak")
    private val context = ContextManager.getContext()

    private val networkObserver = NetworkConnectivityObserver.getInstance(context)

    private val _isConnected = MutableLiveData<ConnectivityObserver.Status>()

    init {
        viewModelScope.launch {
            networkObserver.observe().collect {
                _isConnected.value = it
                if (it != ConnectivityObserver.Status.Available) {
                    // Handle the case when there is no internet connection or losing connection
                    Log.d(TAG, "Connection lost")

                }
            }
        }
    }

    fun redirection(navController: NavController) {
        // Check the current connectivity status
        val isConnected = _isConnected.value == ConnectivityObserver.Status.Available

        // Perform redirection based on the connectivity status
        if (isConnected) {
            // If connected, navigate to the Register screen
            navController.navigate(route = Screen.Register.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        } else {
            // If not connected, navigate to a screen prompting the user to connect to the internet
            navController.navigate(route = Screen.NoInternetConnection.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }
}