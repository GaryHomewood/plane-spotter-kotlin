package uk.co.garyhomewood.planespotter.gallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item.view.*
import uk.co.garyhomewood.planespotter.R
import uk.co.garyhomewood.planespotter.model.atom.Entry

/**
 *
 */
class GalleryAdapter (private val context: Context,
                      private val items: List<Entry>,
                      val clickListener: ItemClickListener) : PagerAdapter() {

    override fun isViewFromObject(view: View?, obj: Any?) = (view == obj)

    override fun getCount() = items.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.gallery_item, container, false)

        Glide.with(context)
                .load(items[position].originalUrl)
                .into(view.item_image)

        view.caption_title.text = items[position].title
        view.caption_meta.text = items[position].description
        view.caption.tag = "caption" + position
        view.setOnClickListener { v -> onClick(v)}
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) = container.removeView(`object` as View)

    private fun onClick(v: View) = clickListener.onItemClick(v.caption)

    interface ItemClickListener {
        fun onItemClick(captionView: View)
    }
}