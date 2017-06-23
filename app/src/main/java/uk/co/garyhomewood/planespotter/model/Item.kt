package uk.co.garyhomewood.planespotter.model

import android.os.Parcelable
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * RSS item (allows parcelling)
 */
@Root(name = "item", strict = false)
class Item: Parcelable {
    var title: String = ""
        @Element(name = "title")
        get
        @Element(name = "title")
        set

    var description: String = ""
        @Element(name = "description")
        get
        @Element(name = "description")
        set

    var subject: String = ""
        @Element(name = "subject")
        get
        @Element(name = "subject")
        set

    var thumbnail: Item.Thumbnail = Thumbnail()
        @Element(name = "thumbnail", required = false)
        get
        @Element(name = "thumbnail", required = false)
        set

    var descriptionText: String = ""
        get() = description.substring(description.indexOf("<br/>") + 5).trim { it <= ' ' }
                .replace("\\n".toRegex(), " ")
                .replace("[^a-zA-Z0-9 ]".toRegex(), "")
                .replace(" {2,}".toRegex(), " ")

    var originalUrl: String = ""
        get() = thumbnail.url.replace("thumbnail", "original")

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {
        val thumbnail: Thumbnail = Thumbnail()
        thumbnail.url = this.thumbnail.url
        with(dest) {
            writeString(title)
            writeString(description)
            writeString(subject)
            writeTypedObject(thumbnail, flags)
        }
    }
    override fun describeContents() = 0

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: android.os.Parcel): Item {
                val item = Item()
                with(item) {
                    title = source.readString()
                    description = source.readString()
                    subject = source.readString()
                    thumbnail = source.readTypedObject<Item.Thumbnail>(Item.Thumbnail.CREATOR)
                }
                return item
            }
            override fun newArray(size: Int) = Array(size, { _ -> Item() })
        }
    }

    class Thumbnail: Parcelable {
        var url: String = ""
            @Attribute(name = "url")
            get
            @Attribute(name = "url")
            set

        override fun writeToParcel(dest: android.os.Parcel, flags: Int) = dest.writeString(url)
        override fun describeContents() = 0

        companion object {
            val CREATOR: Parcelable.Creator<Thumbnail> = object : Parcelable.Creator<Thumbnail> {
                override fun createFromParcel(source: android.os.Parcel): Thumbnail {
                    val thumbnail = Item.Thumbnail()
                    thumbnail.url = source.readString()
                    return thumbnail
                }
                override fun newArray(size: Int): Array<Thumbnail> = Array(size, { _ -> Thumbnail() })
            }
        }
    }
}