package ciandt.com.navigation.view

import android.app.Notification
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
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials
import com.estimote.proximity_sdk.proximity.ProximityAttachment
import com.estimote.proximity_sdk.proximity.ProximityObserver
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder
import com.estimote.proximity_sdk.trigger.ProximityTriggerBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Estimote desiredMeanTriggerDistance
    // https://blog.estimote.com/post/166007374930/the-most-reliable-proximity-tech-now-with-a
    private val DESIRED_MEAN_TRIGGER_DISTANCE = 1.0;

    // Estimote Attachment keys ini
    private val AKEY_BUILDING = "building"
    private val AKEY_FLOOR = "floor"
    private val AKEY_VENUE = "venue"
    private val AKEY_text = "text"
    // Estimote Attachment keys end

    // Estimote Attachment values ini
    private val AVALUE_GARAGE = "garage"
    private val AVALUE_MALL = "mall"
    private val AVALUE_RECEPTION = "reception"
    // Estimote Attachment values end

    // Estimote proximity ini
    private lateinit var notification: Notification
    private lateinit var proximityObserver: ProximityObserver
    private var proximityObservationHandler: ProximityObserver.Handler? = null
    private val cloudCredentials = EstimoteCloudCredentials(BuildConfig.ESTIMOTE_APP_ID_DEBUG, BuildConfig.ESTIMOTE_APP_TOKEN_DEBUG)
    // Estimote proximity end

    // Estimote Toast ini
    private val displayToastAboutMissingRequirements: (List<Requirement>) -> Unit = { Toast.makeText(this, "Unable to start proximity observation. Requirements not fulfilled: ${it.size}", Toast.LENGTH_SHORT).show() }
    private val displayToastAboutError: (Throwable) -> Unit = { Toast.makeText(this, "Error while trying to start proximity observation: ${it.message}", Toast.LENGTH_SHORT).show() }
    // Estimote Toast end

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

    // Estimote actions ini
    private val makeOnEnterAction: (ProximityAttachment) -> Unit = {
        // TODO -- concrete action
        Toast.makeText(this, "Enter action: " + it.getPayload(), Toast.LENGTH_SHORT).show()
    }

    private val makeOnExitAction: (ProximityAttachment) -> Unit = {
        // TODO -- concrete action
        Toast.makeText(this, "Enter action: " + it.getPayload(), Toast.LENGTH_SHORT).show()
    }
    // Estimote actions end

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Estimote notification ini
        notification = NotificationCreator().createNotification(this)
        // Estimote notification end

        /*
        // Estimote ini
        EstimoteSDK.initialize(applicationContext,
                BuildConfig.ESTIMOTE_APP_ID_DEBUG,
                BuildConfig.ESTIMOTE_APP_TOKEN_DEBUG)
        // Estimote end
        */

        // Estimote requirement wizard ini
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                this,
                onRequirementsFulfilled = { startProximityObservation() },
                onRequirementsMissing = displayToastAboutMissingRequirements,
                onError = displayToastAboutError
        )
        // Estimote requirement wizard end

    }

    // Estimote proximity
    override fun onDestroy() {
        super.onDestroy()
        proximityObservationHandler?.stop()
    }

    // Estimote proximity
    private fun startProximityObservation() {
        proximityObserver =
                ProximityObserverBuilder(applicationContext, cloudCredentials)
                        .withScannerInForegroundService(notification)
                        .withBalancedPowerMode()
                        //.withEstimoteSecureMonitoringDisabled()
                        //.withTelemetryReportingDisabled()
                        .withOnErrorAction(displayToastAboutError)
                        .build()

        // Estimote zones ini

        // Recepção
        val receptionRoomZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue(AKEY_VENUE, AVALUE_RECEPTION)
                .inCustomRange(DESIRED_MEAN_TRIGGER_DISTANCE)
                .withOnEnterAction(makeOnEnterAction)
                .withOnExitAction(makeOnExitAction)
                .create()

        // Alameda
        val mallZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue(AKEY_VENUE, AVALUE_MALL)
                .inCustomRange(DESIRED_MEAN_TRIGGER_DISTANCE)
                .withOnEnterAction(makeOnEnterAction)
                .withOnExitAction(makeOnExitAction)
                .create()

        // Garage
        val garageZone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue(AKEY_VENUE, AVALUE_GARAGE)
                .inCustomRange(DESIRED_MEAN_TRIGGER_DISTANCE)
                .withOnEnterAction(makeOnEnterAction)
                .withOnExitAction(makeOnExitAction)
                .create()
        // Estimote zones end

        // Estimote observer ini
        proximityObservationHandler = proximityObserver
                // Adding the Estimote zones...
                .addProximityZones(receptionRoomZone, mallZone, garageZone)
                .start()
        // Estimote observer end

        Toast.makeText(this, "Estimote config done!", Toast.LENGTH_SHORT).show()
    }

}