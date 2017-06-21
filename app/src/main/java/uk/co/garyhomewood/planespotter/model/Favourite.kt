package uk.co.garyhomewood.planespotter.model

import io.realm.RealmObject
import java.util.*

/**
 * RSS item as saved to Realm db
 */
open class Favourite: RealmObject() {
    var title: String = ""
    var description: String = ""
    var subject: String = ""
    var thumbnailUrl: String = ""
    var createdDate: Date = Date()
}