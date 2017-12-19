package uk.co.garyhomewood.planespotter.api

import io.reactivex.Observable
import retrofit2.http.GET
import uk.co.garyhomewood.planespotter.model.atom.Feed


/**
 *
 */
interface PlanesService {
    @GET("feed")
    fun getPlanes(): Observable<Feed>
}