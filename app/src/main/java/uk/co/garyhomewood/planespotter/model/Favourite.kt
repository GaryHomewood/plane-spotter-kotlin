package uk.co.garyhomewood.planespotter.model

import io.realm.RealmObject
import java.util.*

/**
 * Atom RSS entry as saved to Realm db
 */
open class Favourite: RealmObject() {
    var title: String = ""
    var content: String = ""
    var description: String = ""
    var originalUrl: String = ""
    var createdDate: Date = Date()
}