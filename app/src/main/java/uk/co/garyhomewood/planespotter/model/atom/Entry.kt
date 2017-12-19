package uk.co.garyhomewood.planespotter.model.atom

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Atom feed entry
 */
@Root(name = "entry", strict = false)
@SuppressLint("ParcelCreator")
@Parcelize
data class Entry @JvmOverloads constructor(
        @field:Element(name = "title")
        @param:Element(name = "title")
        var title: String = "",

        @field:Element(name = "link")
        @param:Element(name = "link")
        var link: Link? = null,

        @field:Element(name = "updated")
        @param:Element(name = "updated")
        var updated: String = "",

        @field:Element(name = "content")
        @param:Element(name = "content")
        var content: String = ""
) : Parcelable {
    @Transient
    var originalUrl: String = ""
        get() {
            val reg = Regex("https://img.*jpg")
            val match = reg.find(content)
            return match?.value!!.replace("thumbnail", "original")
        }

    @Transient
    var description = ""
        get() = content.substring(content.indexOf("<br/>") + 5)
                .trim { it <= ' ' }
                .replace("\\n".toRegex(), " ")
                .replace("<br/>".toRegex(), "\n")
                .replace(" {2,}".toRegex(), " ")

}