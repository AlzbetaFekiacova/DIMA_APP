package eu.mobcomputing.dima.registration

import android.content.Context

object ContextManager {
    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    fun getContext(): Context {
        return context
    }
}
