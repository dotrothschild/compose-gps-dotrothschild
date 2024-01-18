package com.inzhood.library.kotlingpsdotrothschild

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideActivity(): Activity {
        // This method is here to satisfy Dagger Hilt's requirement for providing an instance
        // of the HiltAppCompatActivity. You don't need to instantiate it yourself.
        throw UnsupportedOperationException("This method should not be called; Dagger Hilt will handle it.")
    }
}
