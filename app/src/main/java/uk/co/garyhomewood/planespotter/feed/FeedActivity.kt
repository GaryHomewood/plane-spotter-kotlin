package uk.co.garyhomewood.planespotter.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_feed.*
import uk.co.garyhomewood.planespotter.R

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_title.text = getString(R.string.app_title)
        view_pager.adapter = SectionsPagerAdapter(supportFragmentManager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = if (position == 0) {
            FeedFragment.newInstance()
        } else
            FeedFragment.newInstance(showFavourites = true)

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> "Latest"
            1 -> "Favourites"
            else -> ""
        }
    }
}
