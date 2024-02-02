package eu.mobcomputing.dima.registration

import android.app.AlertDialog
import android.content.Context
import androidx.navigation.NavController
import eu.mobcomputing.dima.registration.navigation.Screen

class DialogManagerImpl : DialogManager {
    override fun showEmailAlreadyRegisteredDialog(context: Context, navController: NavController) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("E-mail already registered")
        builder.setMessage(
            "I am sorry, the e-mail you are trying to register, I already know. " +
                    "Please provide different e-mail or log in via provided e-mail. " +
                    "If you forgot your password, please click on Yes, I will redirect you to log in page, where you can " +
                    "reset your password."
        )

        builder.setPositiveButton("Yes, go to log in") { _, _ ->
            // User clicked Yes, navigate to login page
            navController.navigate(route = Screen.LogIn.route)
        }

        builder.setNegativeButton("No, create new account") { dialog, _ ->
            // User clicked No, dismiss the dialog
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
        // Implement the actual alert dialog logic here
        // This can use AlertDialog.Builder, similar to your existing code
    }
    // Implement other dialog operations as needed
}