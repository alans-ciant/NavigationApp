package ciandt.com.navigation.view.main

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ciandt.com.navigation.R
import com.crashlytics.android.Crashlytics
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private val SCAN_PERIOD = (60 * 1000).toLong()
    private val SCAN_INTERVAL = (5 * 1000).toLong()

    // 23B
    private val IDENTIFIER_23B = "prédio 23B"
    private val UUID_23B = "B9407F30-F5F8-466E-AFF9-25556B57FE6D"
    // Reception
    private val BEACON_MAJOR_23B_RECEPTION = 15673
    private val BEACON_MINOR_23B_RECEPTION = 2504
    // Garage
    private var BEACON_MAJOR_23B_GARAGE = 46445
    private var BEACON_MINOR_23B_GARAGE = 2555
    // Meeting room
    private var BEACON_MAJOR_23B_MEETING_ROOM = 16097
    private var BEACON_MINOR_23B_MEETING_ROOM = 19559
    // Administration
    private var BEACON_MAJOR_23B_ADMINISTRATION = 62558
    private var BEACON_MINOR_23B_ADMINISTRATION = 229777
    // Marketing
    private var BEACON_MAJOR_23B_MARKETING = 34893
    private var BEACON_MINOR_23B_MARKETING = 52699
    // Human Resources
    private var BEACON_MAJOR_23B_HUMAN_RESOURCES = 64386
    private var BEACON_MINOR_23B_HUMAN_RESOURCES = 39261
    // Test
    private var BEACON_MAJOR_23B_TEST = 11111
    private var BEACON_MINOR_23B_TEST = 7777

    // MALL
    private val IDENTIFIER_MALL = "alameda Ci&T"
    private val UUID_MALL = "687DBC06-BE1C-424C-B0EC-942B2A729674"
    // Entrance
    private val BEACON_MAJOR_ENTRANCE_MALL = 52381
    private val BEACON_MINOR_ENTRANCE_MALL = 22058

    // String
    private val DOUBLE_DOT = ":"

    private lateinit var beaconManager: BeaconManager
    private lateinit var region23B: BeaconRegion
    private lateinit var regionMall: BeaconRegion

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            val currentItemId = navigation.menu.getItem(position).itemId
            if (currentItemId != navigation.selectedItemId) {
                navigation.menu.getItem(position).isChecked = true
                navigation.menu.findItem(navigation.selectedItemId).isChecked = false
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        beaconManager.setRangingListener(BeaconManager.BeaconRangingListener { region, list ->
            if (!list.isEmpty()) {
                val nearestBeacon = list[0]
                val places = placesNearBeacon(nearestBeacon)
                val txt = "Região: " + region.identifier + " " + places.toString()
                Log.d("DEBUG NEAREST BEACON: ", nearestBeacon.toString())

                if (txt.equals(homeTxt)) {
                    Log.d("DEBUG RANGING: ", "Ignoring...")
                } else {
                    homeTxt = txt
                    message.setText(homeTxt)
                    showNotification("Navigation", txt)
                    Log.d("DEBUG RANGING: ", homeTxt)
                }
        navigation.setOnNavigationItemSelectedListener { menuItem ->
            selectMenuItem(menuItem)
        }

        val adapter = MainFragmentPagaAdapter(supportFragmentManager, applicationContext)

        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.adapter = adapter

        beaconManager = BeaconManager(this)
        beaconManager.setForegroundScanPeriod(SCAN_PERIOD, SCAN_INTERVAL)
        beaconManager.setRangingListener(BeaconManager.BeaconRangingListener { region, list ->
            for (beacon in list) {
                val nearestBeacon = placesNearBeacon(list[0])
                val places = placesNearBeacon(beacon)

                adapter.updateFragments(region, nearestBeacon, places)

                Timber.d(beacon.toString())
            }
        })

        region23B = BeaconRegion(IDENTIFIER_23B, UUID.fromString(UUID_23B), null, null)
        regionMall = BeaconRegion(IDENTIFIER_MALL, UUID.fromString(UUID_MALL), null, null)
    }

    private fun selectMenuItem(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_home -> viewPager.currentItem = 0
            R.id.navigation_history -> viewPager.currentItem = 1
            R.id.navigation_directions -> viewPager.currentItem = 2
            else -> viewPager.currentItem = 0
        }

        return true;
    }

    override fun onResume() {
        super.onResume()

        SystemRequirementsChecker.checkWithDefaultDialogs(this)
        beaconManager.connect({
            beaconManager.startRanging(region23B)
            beaconManager.startRanging(regionMall)
        })
    }

    override fun onPause() {
        beaconManager.stopRanging(region23B)
        beaconManager.stopRanging(regionMall)

        super.onPause()
    }

    private fun placesNearBeacon(beacon: Beacon): List<String>? {
        Timber.d(beacon.toString())

        val placesByBeacons = hashMapPlaces()
        val beaconKey = String.format("%d:%d", beacon.major, beacon.minor)

        return if (placesByBeacons.containsKey(beaconKey)) {
            placesByBeacons.get(beaconKey)
        } else emptyList()
    }

    private fun hashMapPlaces(): HashMap<String, List<String>> {
        val placesByBeacons = HashMap<String, List<String>>()

        // 23B Reception
        placesByBeacons[BEACON_MAJOR_23B_RECEPTION.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_RECEPTION] = object : ArrayList<String>() {
            init {
                add("Porta de entrada do prédio")
                add("Escada de acesso ao primeiro andar")
                add("Garagem")
                add("Porta de acesso interno com biometria")
            }
        }
        // 23B Meeting room
        placesByBeacons[BEACON_MAJOR_23B_MEETING_ROOM.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_MEETING_ROOM] = object : ArrayList<String>() {
            init {
                add("Recepção")
                add("Sala de reunião")
            }
        }
        // 23B Garage
        placesByBeacons[BEACON_MAJOR_23B_GARAGE.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_GARAGE] = object : ArrayList<String>() {
            init {
                add("Recepção")
                add("Porta de acesso interno com biometria")
            }
        }
        // 23B Marketing
        placesByBeacons[BEACON_MAJOR_23B_MARKETING.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_MARKETING] = object : ArrayList<String>() {
            init {
                add("Porta de acesso interno com biometria")
                add("Marketing")
                add("Salas de reunião")
                add("Administrativo")
            }
        }
        // 23B Administration
        placesByBeacons[BEACON_MAJOR_23B_ADMINISTRATION.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_ADMINISTRATION] = object : ArrayList<String>() {
            init {
                add("Marketing")
                add("Salas de reunião")
                add("Banheiros")
                add("Recursos Humanos")
            }
        }
        // 23B Human Resources
        placesByBeacons[BEACON_MAJOR_23B_HUMAN_RESOURCES.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_HUMAN_RESOURCES] = object : ArrayList<String>() {
            init {
                add("Administrativo")
                add("Quitanda Geek")
                add("Cozinha")
            }
        }
        // TEST
        placesByBeacons[BEACON_MAJOR_23B_TEST.toString() +
                DOUBLE_DOT +
                BEACON_MINOR_23B_TEST] = object : ArrayList<String>() {
            init {
                add("Projeto Honda")
                add("Descriçao Teste")
            }
        }
        // 23B Mall
        placesByBeacons.put(
                ((BEACON_MAJOR_ENTRANCE_MALL).toString() +
                        DOUBLE_DOT +
                        BEACON_MINOR_ENTRANCE_MALL), object : ArrayList<String>() {
            init {
                add("Escada de acesso a rua")
                add("Àrea coberta com mesas e sofás")
                add("Porta de acesso ao prédio 23B")
            }
        })
        return placesByBeacons
    }

    private fun showNotification(title: String, message: String) {
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivities(this, 0,
                arrayOf(notifyIntent), PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}