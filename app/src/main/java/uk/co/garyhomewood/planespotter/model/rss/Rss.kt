package uk.co.garyhomewood.planespotter.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

/**
 * RSS root
 */
@Root(name = "rss", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
        Namespace(reference = "http://purl.org/dc/elements/1.1/", prefix = "dc"),
        Namespace(reference = "http://search.yahoo.com/mrss/", prefix = "media")
)
class Rss {
    var channel: Channel? = null
        @Element(name = "channel")
        get
        @Element(name = "channel")
        set
}
