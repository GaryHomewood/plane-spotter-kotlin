package uk.co.garyhomewood.planespotter.model.atom

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Attribute

@SuppressLint("ParcelCreator")
@Parcelize
data class Link @JvmOverloads constructor(
        @field:Attribute(name = "href")
        @param:Attribute(name = "href")
        var href: String = ""
): Parcelable