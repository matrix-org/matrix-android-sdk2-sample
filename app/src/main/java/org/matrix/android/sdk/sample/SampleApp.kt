package org.matrix.android.sdk.sample

import android.app.Application
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.MatrixConfiguration
import timber.log.Timber

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // You should first init Matrix before using it
        Matrix.initialize(this, MatrixConfiguration())
        // It returns a singleton
        val matrix = Matrix.getInstance(this)
        // You can then grab the authentication service and search for a known session
        val lastSession = matrix.authenticationService().getLastAuthenticatedSession()
        if (lastSession != null) {
            SessionHolder.currentSession = lastSession
            // Don't forget to open the session and start syncing.

            lastSession.open()
            lastSession.startSync(true)
        }
    }

}