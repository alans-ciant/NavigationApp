package ciandt.com.navigation.view.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ciandt.com.navigation.R
import ciandt.com.navigation.model.Beacon
import kotlinx.android.synthetic.main.beacon_cardview.view.*
import java.util.*

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 29/07/2018
 */
class BeaconAdapter(
        private val context: Context,
        private val emptyView: View,
        private val listener: (Beacon) -> Unit
) : RecyclerView.Adapter<BeaconAdapter.ViewHolder>() {

    private val beacons = LinkedList<Beacon>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(beacon: Beacon, listener: (Beacon) -> Unit) = with(itemView) {

            textViewRegion.text = beacon.region
            textViewPlace.text = beacon.place
            textViewDescription.text = beacon.description

            setOnClickListener {
                listener(beacon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.beacon_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = beacons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = beacons[position]
        holder.bindView(item, listener)
    }

    fun insert(beacon: Beacon) {
        if (beacons.contains(beacon)) return

        beacons.addFirst(beacon)
        notifyDataSetChanged()
    }

}