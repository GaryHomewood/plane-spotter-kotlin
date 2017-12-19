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
import uk.co.garyhomewood.planespotter.model.atom.Entry
import uk.co.garyhomewood.planespotter.model.atom.Feed

class FeedPresenter(var view: FeedInterface.View,
                    private var service: PlanesService,
                    var realm: Realm) : FeedInterface.UserActions {

    override fun loadFeed() {
        val call: Observable<Feed> = service.getPlanes()
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showProgressIndicator(true) }
                .doFinally { view.showProgressIndicator(false) }
                .subscribe({
                    feed: Feed? -> view.showItems(feed?.entries!!)
                }, {
                    t: Throwable? -> Timber.e(t)
                })
    }

    override fun loadFavourites() {
        view.showProgressIndicator(true)

        val favourites: RealmResults<Favourite> = realm
                .where(Favourite::class.java)
                ?.findAllSorted("createdDate", Sort.DESCENDING) as RealmResults<Favourite>

        val items: List<Entry> = favourites.map { favourite ->
            val item = Entry()
            with(item) {
                title = favourite.title
                content = favourite.content
                description = favourite.description
                originalUrl = favourite.originalUrl
            }
            item
        }

        view.showItems(items)
        view.showProgressIndicator(false)
    }

    override fun showGallery(items: List<Entry>, selectedItem: Int) {
        view.showGallery(items, selectedItem)
    }
}