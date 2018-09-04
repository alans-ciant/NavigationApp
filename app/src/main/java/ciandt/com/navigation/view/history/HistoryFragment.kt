package ciandt.com.navigation.view.history

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ciandt.com.navigation.R
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import kotlinx.android.synthetic.main.main_beacon_rv.*

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/09/2018
 */
class HistoryFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

    private val beaconAdapter by lazy {
        context?.let { context ->
            BeaconAdapter(context, emptyview) { beacon ->
                Toast.makeText(context, "$beacon", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewBeacons.layoutManager = LinearLayoutManager(context)
        recyclerViewBeacons.adapter = beaconAdapter
    }

    fun updateState(region: BeaconRegion, places: List<String>?) {
        beaconAdapter?.insert(ciandt.com.navigation.model.Beacon(
                region.identifier,
                places?.get(0) ?: "Beacon not found",
                places?.toString() ?: "Beacon not found"
        ))
    }
}
