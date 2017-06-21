package uk.co.garyhomewood.planespotter.api

import io.reactivex.Observable
import retrofit2.http.GET
import uk.co.garyhomewood.planespotter.model.Rss


/**
 *
 */
interface PlanesService {
    @GET("search.php?output=rss")
    fun getPlanes(): Observable<Rss>
}