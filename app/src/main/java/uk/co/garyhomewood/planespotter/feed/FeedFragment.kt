package uk.co.garyhomewood.planespotter.feed

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed.*
import org.parceler.Parcels
import uk.co.garyhomewood.planespotter.PlaneSpotterApp
import uk.co.garyhomewood.planespotter.R
import uk.co.garyhomewood.planespotter.di.Injector
import uk.co.garyhomewood.planespotter.gallery.GalleryActivity
import uk.co.garyhomewood.planespotter.model.atom.Entry

/**
 *
 */
class FeedFragment : Fragment(), FeedInterface.View {

    private val KEY_ITEMS = "KEY_ITEMS"
    private val KEY_SELECTED_ITEM = "KEY_SELECTED_ITEM"

    private var favourites: Boolean = false
    private var itemClickListener = object : FeedAdapter.ItemClickListener {
        override fun onItemClick(planes: List<Entry>, selectedItem: Int) {
            userActionsListener?.showGallery(planes, selectedItem)
        }
    }

    var userActionsListener: FeedInterface.UserActions? = null

    companion object {
        fun newInstance(): FeedFragment {
            return newInstance(showFavourites = false)
        }

        fun newInstance(showFavourites: Boolean): FeedFragment {
            val frag = FeedFragment()
            val args = Bundle()
            args.putBoolean("favourites", showFavourites)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favourites = arguments.getBoolean("favourites")
        userActionsListener = FeedPresenter(this, Injector.providePlanesService(), PlaneSpotterApp.instance.realm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.setOnRefreshListener { loadData() }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() = if (favourites) userActionsListener?.loadFavourites() else userActionsListener?.loadFeed()

    override fun showItems(items: List<Entry>) {
        val numberOfColumns: Int = if (resources.configuration.orientation == OrientationHelper.VERTICAL) 2 else 3
        item_list.layoutManager = GridLayoutManager(context, numberOfColumns)
        item_list.adapter = FeedAdapter(items, itemClickListener)
    }

    override fun showProgressIndicator(active: Boolean) {
        refresh_layout.post { refresh_layout.isRefreshing = active }
    }

    override fun showGallery(items: List<Entry>, selectedItem: Int) {
        val i = Intent(context, GalleryActivity::class.java)
        i.putExtra(KEY_ITEMS, Parcels.wrap(items))
        i.putExtra(KEY_SELECTED_ITEM, selectedItem)
        startActivity(i)
    }
}
