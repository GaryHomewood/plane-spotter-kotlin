package uk.co.garyhomewood.planespotter

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber


/**
 *
 */
class PlaneSpotterApp : Application() {

    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.i("Creating Application...")

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("planespotter.realm")
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }

    fun hasNetwork(): Boolean {
        return instance.isConnected()
    }

    fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    companion object {
        lateinit var instance: PlaneSpotterApp
            private set
    }
}