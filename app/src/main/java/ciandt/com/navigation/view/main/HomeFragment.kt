package ciandt.com.navigation.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ciandt.com.navigation.R
import ciandt.com.navigation.view.utils.SpannableStringCreator
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import kotlinx.android.synthetic.main.beacon_cardview.*

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/09/2018
 */
class HomeFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun updateState(region: BeaconRegion, nearestBeacon: List<String>?) {
        context?.let {
            val beacon = ciandt.com.navigation.model.Beacon(
                    region.identifier,
                    nearestBeacon?.get(0) ?: "Beacon not found",
                    nearestBeacon?.toString() ?: "Beacon not found"
            )

            textViewRegion.text = SpannableStringCreator()
                    .appendBold(R.string.labelRegion, it)
                    .appendSpace(beacon.region)
                    .toSpannableString()

            textViewPlace.text = SpannableStringCreator()
                    .appendBold(R.string.labelPlace, it)
                    .appendSpace(beacon.place)
                    .toSpannableString()

            textViewDescription.text = SpannableStringCreator()
                    .appendBold(R.string.labelDescription, it)
                    .appendSpace(beacon.description)
                    .toSpannableString()
        }
    }
}
