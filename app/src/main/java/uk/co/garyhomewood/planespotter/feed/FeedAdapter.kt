package uk.co.garyhomewood.planespotter.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feed_item.view.*
import uk.co.garyhomewood.planespotter.R
import uk.co.garyhomewood.planespotter.feed.FeedAdapter.ViewHolder
import uk.co.garyhomewood.planespotter.model.atom.Entry

/**
 *
 */
class FeedAdapter(var planes: List<Entry>,
                  private var itemClickListener: FeedAdapter.ItemClickListener) : android.support.v7.widget.RecyclerView.Adapter<ViewHolder>() {

    var context : android.content.Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, index: Int) = holder.bindPlane(planes[index], itemClickListener)

    override fun getItemCount(): Int = planes.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var itemClickListener : FeedAdapter.ItemClickListener? = null

        fun bindPlane(plane: Entry, listener: FeedAdapter.ItemClickListener) {
            with(itemView) {
                item_title.text = plane.title
                item_description.text = plane.description
                Glide.with(context)
                        .load(plane.originalUrl)
                        .fitCenter()
                        .into(item_thumbnail)
                itemView.setOnClickListener { v -> onClick(v) }
            }
            itemClickListener = listener
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(planes, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(planes : List<Entry>, selectedItem : Int)
    }
}