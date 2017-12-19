package uk.co.garyhomewood.planespotter.model.rss

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * RSS channel of items
 */
@Root(name = "channel", strict = false)
class Channel {
    var items: List<Item>? = null
        @ElementList(name = "item", inline = true)
        get
        @ElementList(name = "item", inline = true)
        set
}

