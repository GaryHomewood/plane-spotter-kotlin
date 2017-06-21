package uk.co.garyhomewood.planespotter.gallery

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_gallery.*
import org.parceler.Parcels
import timber.log.Timber
import uk.co.garyhomewood.planespotter.PlaneSpotterApp
import uk.co.garyhomewood.planespotter.R
import uk.co.garyhomewood.planespotter.model.Favourite
import uk.co.garyhomewood.planespotter.model.Item
import java.util.*


/**
 *
 */
class GalleryActivity : AppCompatActivity() {

    val KEY_ITEMS = "KEY_ITEMS"
    val KEY_SELECTED_ITEM = "KEY_SELECTED_ITEM"
    val UI_ANIMATION_DELAY: Long = 100

    var menu: Menu? = null
    var isFullscreen = false
    val fullScreenHandler = Handler()
    val hideUI: Runnable = Runnable {
        supportActionBar?.hide()
        view_pager.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    val showUI: Runnable = Runnable {
        supportActionBar?.show()
        view_pager.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        view_pager.visibility = View.VISIBLE
    }

    val captionFullscreenOffset: Float = 100 * Resources.getSystem().displayMetrics.density

    val itemClickListener = object : GalleryAdapter.ItemClickListener {
        override fun onItemClick(captionView: View) {
            if (isFullscreen) {
                captionView.animate().translationY(0F)
                exitFullscreen()
            } else {
                captionView.animate().translationY(captionFullscreenOffset)
                enterFullscreen()
            }
        }
    }

    var items = mutableListOf<Item>()
    var selectedItem: Int = 0
    var isFavourite: Boolean = false
    lateinit var adapter: GalleryAdapter
    lateinit var makeFavouriteIcon: Drawable
    lateinit var isFavoriteIcon: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        items = Parcels.unwrap(intent.getParcelableExtra(KEY_ITEMS))
        selectedItem = intent.getIntExtra(KEY_SELECTED_ITEM, 0)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        makeFavouriteIcon = ContextCompat.getDrawable(baseContext, R.drawable.ic_bookmark_border)
        isFavoriteIcon = ContextCompat.getDrawable(baseContext, R.drawable.ic_bookmark)

        adapter = GalleryAdapter(this, items, clickListener = itemClickListener)
        view_pager.adapter = adapter
        view_pager.currentItem = selectedItem
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset > 0) {
                    // show or hide caption
                    setCaptionY(position)
                    setCaptionY(position + 1)
                }
            }

            override fun onPageSelected(position: Int) {
                isFavourite = checkIfFavourite(position)
            }

            fun setCaptionY(idx: Int) {
                val captionView = view_pager.findViewWithTag("caption" + idx)
                if (isFullscreen) {
                    captionView.translationY = captionFullscreenOffset
                } else {
                    captionView.translationY = 0F
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.gallery_menu, menu)
        this.menu = menu
        isFavourite = checkIfFavourite(selectedItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.action_favourite -> consume { if (isFavourite) removeFavourite() else addFavourite() }
                R.id.action_settings -> consume { }
                else -> super.onOptionsItemSelected(item)
            }

    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    fun checkIfFavourite(position: Int): Boolean {
        val item = items[position]

        val result: RealmResults<Favourite>? = PlaneSpotterApp.instance.realm
                .where(Favourite::class.java)
                .equalTo("thumbnailUrl", item.thumbnail.url)
                .findAll()

        if (result?.size == 1) {
            menu?.getItem(0)?.icon = isFavoriteIcon
            return true
        } else {
            menu?.getItem(0)?.icon = makeFavouriteIcon
            return false
        }
    }

    fun addFavourite() {
        val item = items[view_pager.currentItem]

        PlaneSpotterApp.instance.realm.executeTransactionAsync(
                {
                    realmBackground ->
                    val favourite = realmBackground.createObject(Favourite::class.java)
                    with(favourite) {
                        title = item.title
                        description = item.description
                        subject = item.subject
                        thumbnailUrl = item.thumbnail.url
                        createdDate = Date(System.currentTimeMillis())
                    }
                },
                {
                    isFavourite = true
                    adapter.notifyDataSetChanged()
                    menu?.getItem(0)?.icon = isFavoriteIcon
                },
                {
                    error ->
                    Timber.e(error.message)
                }
        )
    }

    fun removeFavourite() {
        val item = items[view_pager.currentItem]

        PlaneSpotterApp.instance.realm.executeTransactionAsync(
                {
                    realm ->
                    val result: RealmResults<Favourite> = realm
                            .where(Favourite::class.java)
                            .equalTo("thumbnailUrl", item.thumbnail.url)
                            .findAll()
                    result.deleteFirstFromRealm()
                },
                {
                    isFavourite = false
                    adapter.notifyDataSetChanged()
                    menu?.getItem(0)?.icon = makeFavouriteIcon
                },
                {
                    error ->
                    Timber.e(error.message)
                })
    }

    fun enterFullscreen() {
        isFullscreen = true
        fullScreenHandler.removeCallbacks(showUI)
        fullScreenHandler.postDelayed(hideUI, UI_ANIMATION_DELAY)
    }

    fun exitFullscreen() {
        isFullscreen = false
        fullScreenHandler.removeCallbacks(hideUI)
        fullScreenHandler.postDelayed(showUI, UI_ANIMATION_DELAY)
    }
}