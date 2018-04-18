package ciandt.com.navigation.view.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ciandt.com.navigation.R
import ciandt.com.navigation.model.BeaconTo

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 15/04/2018
 */
class BeaconAdapter(val context: Context, val itemClickListener: ItemClickListener, val beacons: List<BeaconTo>, val emptyView: View) :
        RecyclerView.Adapter<BeaconAdapter.ViewHolder>() {

    val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)

        emptyView.setVisibility(if (beacons.isEmpty()) View.VISIBLE else View.INVISIBLE)
    }

    interface ItemClickListener {
        fun onClick(beacon: BeaconTo)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: View
        val nameTextView: TextView
        val descriptionView: TextView

        init {
            root = itemView.findViewById(R.id.root)
            nameTextView = itemView.findViewById(R.id.name_textview)
            descriptionView = itemView.findViewById(R.id.description_textview)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.beacon_rv_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beacon = beacons.get(position)

        holder.nameTextView.setText(beacon.name)
        holder.descriptionView.setText(beacon.description)
        holder.root.setOnClickListener({ itemClickListener.onClick(beacon) })
    }

    override fun getItemCount(): Int {
        return beacons.size
    }
}