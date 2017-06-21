package uk.co.garyhomewood.planespotter.feed

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import timber.log.Timber
import uk.co.garyhomewood.planespotter.api.PlanesService
import uk.co.garyhomewood.planespotter.model.Favourite
import uk.co.garyhomewood.planespotter.model.Item
import uk.co.garyhomewood.planespotter.model.Rss

class FeedPresenter(var view: FeedInterface.View, var service: PlanesService, var realm: Realm) : FeedInterface.UserActions {

    /**
     * Load favourites from Realm db
     */
    override fun loadFavourites() {
        view.showProgressIndicator(true)

        val favourites: RealmResults<Favourite> = realm
                .where(Favourite::class.java)
                ?.findAllSorted("createdDate", Sort.DESCENDING) as RealmResults<Favourite>

        val items: List<Item> = favourites.map { favourite ->
            val item: Item = Item()
            with(item) {
                title = favourite.title
                description = favourite.description
                thumbnail = Item.Thumbnail()
                thumbnail.url = favourite.thumbnailUrl
                subject = favourite.subject
            }
            item
        }

        view.showItems(items)
        view.showProgressIndicator(false)
    }

    /**
     * Load RSS feed
     */
    override fun loadFeed() {
        val call: Observable<Rss> = service.getPlanes()

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgressIndicator(true) }
                .doFinally { view.showProgressIndicator(false) }
                .subscribe({
                    rss: Rss? ->
                    view.showItems(rss?.channel?.items!!)
                }, {
                    t: Throwable? ->
                    Timber.e(t)
                })
    }

    override fun showGallery(items: List<Item>, selectedItem: Int) {
        view.showGallery(items, selectedItem)
    }
}