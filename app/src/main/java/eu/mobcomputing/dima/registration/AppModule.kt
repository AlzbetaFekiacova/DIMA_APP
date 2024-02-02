package eu.mobcomputing.dima.registration

import android.app.Application
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthManager(): FirebaseAuthManager {
        return FirebaseAuthManagerImpl()
    }

    @Provides
    @Singleton
    fun provideDialogManager(application: Application): DialogManager {
        // Provide your implementation of DialogManager
        return DialogManagerImpl()
    }
}
