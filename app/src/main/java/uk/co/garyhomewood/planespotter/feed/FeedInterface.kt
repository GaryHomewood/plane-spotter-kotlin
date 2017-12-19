package uk.co.garyhomewood.planespotter.feed

import uk.co.garyhomewood.planespotter.model.atom.Entry

/**
 * Interface for feed view
 *
 */
interface FeedInterface {
    interface View {
        fun showProgressIndicator(active : Boolean)
        fun showItems(items: List<Entry>)
        fun showGallery(items: List<Entry>, selectedItem : Int)
    }

    interface UserActions {
        fun loadFeed()
        fun loadFavourites()
        fun showGallery(items: List<Entry>, selectedItem : Int)
    }
}
