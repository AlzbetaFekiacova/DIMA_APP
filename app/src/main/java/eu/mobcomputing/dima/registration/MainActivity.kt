package eu.mobcomputing.dima.registration

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import eu.mobcomputing.dima.registration.viewmodels.HomeViewModel
import eu.mobcomputing.dima.registration.navigation.SetUpNavGraph
import eu.mobcomputing.dima.registration.ui.theme.RegistrationTheme

/**
 * Main entry point for the application.
 * Configures the navigation and sets up the initial screen.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val homeViewModel: HomeViewModel by viewModels()

    /**
     * Called when the activity is first created.
     * Initializes the content of the activity and sets up the navigation graph.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *     being shut down, this Bundle contains the data it most recently supplied in
     *     [onSaveInstanceState].
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and the FCM
            Log.d("FCM", token.toString());
        })

        super.onCreate(savedInstanceState)
        setContent {
            RegistrationTheme {
                navController = rememberNavController()
                SetUpNavGraph(navController = navController, homeViewModel)
            }
        }
    }
}


