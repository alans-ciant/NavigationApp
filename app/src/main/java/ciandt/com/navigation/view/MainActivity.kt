package ciandt.com.navigation.view

import android.app.Notification
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ciandt.com.navigation.BuildConfig
import ciandt.com.navigation.R
import ciandt.com.navigation.model.NotificationCreator
import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials
import com.estimote.proximity_sdk.proximity.ProximityObserver
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder
import com.estimote.proximity_sdk.trigger.ProximityTriggerBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val context: Context get() = this
    // Estimote proximity ini
    private lateinit var notification: Notification
    private lateinit var proximityObserver: ProximityObserver
    private var proximityObservationHandler: ProximityObserver.Handler? = null
    private val cloudCredentials = EstimoteCloudCredentials(BuildConfig.ESTIMOTE_APP_ID_DEBUG, BuildConfig.ESTIMOTE_APP_TOKEN_DEBUG)
    // Estimote proximity end

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                message.setText(R.string.title_history)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_directions -> {
                message.setText(R.string.title_directions)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Estimote ini
        EstimoteSDK.initialize(applicationContext, BuildConfig.ESTIMOTE_APP_ID, BuildConfig.ESTIMOTE_APP_TOKEN)
        EstimoteSDK.enableDebugLogging(true)
        // Estimote end

        // Estimote proximity ini
        notification = NotificationCreator().createNotification(this)

        proximityObserver =
                ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                        .withScannerInForegroundService(notification)
                        .withBalancedPowerMode()
                        .build();
        // Estimote proximity end

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        proximityObservationHandler = proximityObserver
                //.addProximityZones(venueZone, mintDeskZone, blueberryDeskZone)
                .start()
    }

    // Estimote proximity
    override fun onDestroy() {
        super.onDestroy()
        proximityObservationHandler?.stop()
    }

    // Estimote proximity
    private fun showTriggerSetupDialog() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Toast.makeText(this, "ProximityTrigger works only on devices with Android 8.0+", Toast.LENGTH_SHORT).show()
        } else {
            createTriggerDialog().show()
        }
    }

    private fun createTriggerDialog() =
            AlertDialog.Builder(this)
                    .setTitle("ProximityTrigger setup")
                    .setMessage("The ProximityTrigger will display your notification when the user" +
                            " has entered the proximity of beacons. " +
                            "You can leave your beacons range, enable the trigger, kill your app, " +
                            "and go back - see what happens!")
                    .setPositiveButton("Enable", { _, _ ->
                        val notification = NotificationCreator().createTriggerNotification(this)
                        ProximityTriggerBuilder(this)
                                .displayNotificationWhenInProximity(notification)
                                .build()
                                .start()
                        Toast.makeText(this, "Trigger enabled!", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("Disable", { _, _ ->
                        val notification = NotificationCreator().createTriggerNotification(this)
                        ProximityTriggerBuilder(this).displayNotificationWhenInProximity(notification)
                                .build()
                                .start().stop()
                        Toast.makeText(this, "Trigger disabled.", Toast.LENGTH_SHORT).show()
                    }).create()
}