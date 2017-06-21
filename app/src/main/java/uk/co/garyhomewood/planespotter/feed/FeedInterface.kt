package uk.co.garyhomewood.planespotter.feed

/**
 * Interface for feed view
 *
 */
interface FeedInterface {
    interface View {
        fun showProgressIndicator(active : Boolean)
        fun showItems(items: List<uk.co.garyhomewood.planespotter.model.Item>)
        fun showGallery(items: List<uk.co.garyhomewood.planespotter.model.Item>, selectedItem : Int)
    }

    interface UserActions {
        fun loadFeed()
        fun loadFavourites()
        fun showGallery(items: List<uk.co.garyhomewood.planespotter.model.Item>, selectedItem : Int)
    }
}
