package ciandt.com.navigation

import android.app.Application
import com.estimote.coresdk.common.config.EstimoteSDK
import timber.log.Timber

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 15/04/2018
 */
class NavigationAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        EstimoteSDK.initialize(applicationContext,
                BuildConfig.ESTIMOTE_APP_ID,
                BuildConfig.ESTIMOTE_APP_TOKEN)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            EstimoteSDK.enableDebugLogging(true)
            Timber.d("EstimoteSDK is in debug mode!!!")
        }
    }

}