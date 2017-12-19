package uk.co.garyhomewood.planespotter.model.atom

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

/**
 * Atom feed root
 */
@Root(name = "feed", strict = false)
@NamespaceList(Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"))
data class Feed(
        @field:ElementList(name = "entry", inline = true)
        @param:ElementList(name = "entry", inline = true)
        val entries: List<Entry>? = null
)