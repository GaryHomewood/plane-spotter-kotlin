package uk.co.garyhomewood.planespotter.model.atom

import org.junit.Assert
import org.junit.Test

@Suppress("IllegalIdentifier")
class EntryTest {
    @Test
    fun `test image source is extracted from content`() {

        val entry = Entry()
        entry.content = "<a href=\"https://www.planespotters.net/photo/801298/7t-vjr-air-algrie-boeing-737-6d6\"><img src=\"https://img.planespotters.net/photo/801000/thumbnail/7t-vjr-air-algrie-boeing-737-6d6_PlanespottersNet_801298.jpg?t=1513257235\" /></a><br/>\n            7T-VJR Air AlgÃ©rie Boeing 737-6D6 by leandro"

        Assert.assertEquals(
                "https://img.planespotters.net/photo/801000/thumbnail/7t-vjr-air-algrie-boeing-737-6d6_PlanespottersNet_801298.jpg",
                entry.originalUrl)
    }
}