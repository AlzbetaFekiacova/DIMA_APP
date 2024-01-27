package eu.mobcomputing.dima.registration

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import eu.mobcomputing.dima.registration.navigation.SetUpNavGraph
import eu.mobcomputing.dima.registration.notification.MyNotificationWorker
import eu.mobcomputing.dima.registration.ui.theme.RegistrationTheme
import java.util.concurrent.TimeUnit


/**
 * Main entry point for the application.
 * Configures the navigation and sets up the initial screen.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    /**
     * Called when the activity is first created.
     * Initializes the content of the activity and sets up the navigation graph.
     * Fetches the FCM token.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *     being shut down, this Bundle contains the data it most recently supplied in
     *     [onSaveInstanceState].
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        // Get instance of FM to fetch the FCM registration token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and the FCM
            Log.d("FCM", token.toString())
        })

        val workRequest = PeriodicWorkRequestBuilder<MyNotificationWorker>(
            repeatInterval = 24, // Repeat every 24 hours
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        super.onCreate(savedInstanceState)
        ContextManager.initialize(applicationContext)
        setContent {
            RegistrationTheme {
                navController = rememberNavController()
                SetUpNavGraph(navController = navController)
            }
        }
    }
}


