package ciandt.com.navigation.view.main

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ciandt.com.navigation.R
import ciandt.com.navigation.view.directions.DirectionsFragment
import ciandt.com.navigation.view.history.HistoryFragment
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 05/08/2018
 */
class MainFragmentPagaAdapter(private val fragmentManager: FragmentManager,
                              private val context: Context)
    : FragmentPagerAdapter(fragmentManager) {

    private val homeFragment = HomeFragment.newInstance()
    private val historyFragment = HistoryFragment.newInstance()
    private val directionsFragment = DirectionsFragment.newInstance()

    override fun getItem(position: Int) =
            when (position) {
                0 -> homeFragment
                1 -> historyFragment
                2 -> directionsFragment
                else -> homeFragment
            }

    override fun getCount() = context.resources.getInteger(R.integer.bottom_nav_length)

    fun updateFragments(region: BeaconRegion, nearestBeacon: Beacon, places: List<String>?) {
        historyFragment.updateState(region, places)
        //homeFragment.updateState()
    }
}