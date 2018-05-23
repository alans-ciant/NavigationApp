package ciandt.com.navigation

import android.app.Application
import android.util.Log
import com.estimote.coresdk.common.config.EstimoteSDK

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 15/04/2018
 */
class NavigationAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Log.d("DEBUG: ", "EstimoteSDK is in debug mode!!!")
            EstimoteSDK.enableDebugLogging(true)
        }
    }

}