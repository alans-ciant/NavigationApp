package ciandt.com.navigation.view.history

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ciandt.com.navigation.R
import ciandt.com.navigation.model.Beacon
import ciandt.com.navigation.view.utils.SpannableStringCreator
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

    companion object {
        const val ITEMS_LIMIT = 5 // TODO Firebase
    }

    private val beacons = LinkedList<Beacon>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(beacon: Beacon, listener: (Beacon) -> Unit) = with(itemView) {

            textViewRegion.text = SpannableStringCreator()
                    .appendBold(R.string.labelRegion, context)
                    .appendSpace(beacon.region)
                    .toSpannableString()

            textViewPlace.text = SpannableStringCreator()
                    .appendBold(R.string.labelPlace, context)
                    .appendSpace(beacon.place)
                    .toSpannableString()

            textViewDescription.text = SpannableStringCreator()
                    .appendBold(R.string.labelDescription, context)
                    .appendSpace(beacon.description)
                    .toSpannableString()

            setOnClickListener {
                listener(beacon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.beacon_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = beacons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = beacons[position]
        holder.bindView(item, listener)
    }

    fun insert(beacon: Beacon) {
        if (beacons.contains(beacon)) beacons.remove(beacon)
        if (beacons.size >= ITEMS_LIMIT) beacons.removeLast()

        beacons.addFirst(beacon)

        notifyDataSetChanged()
    }

}