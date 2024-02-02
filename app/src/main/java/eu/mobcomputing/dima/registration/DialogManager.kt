package eu.mobcomputing.dima.registration

import android.content.Context
import androidx.navigation.NavController

interface DialogManager {
    fun showEmailAlreadyRegisteredDialog(context: Context, navController: NavController)
    // Add other dialog operations as needed
}
