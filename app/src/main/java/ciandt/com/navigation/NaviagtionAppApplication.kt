package ciandt.com.navigation

import android.app.Application
import com.estimote.coresdk.common.config.EstimoteSDK

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 15/04/2018
 */
class NaviagtionAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            EstimoteSDK.enableDebugLogging(true)
        }
    }

}