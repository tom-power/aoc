package aoc22

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test
    fun `can transpose symetric`() {
        assertEquals(
            /* expected = */ listOf(
                listOf(" ", "N", "Z"),
                listOf("D", "C", "M"),
                listOf(" ", " ", "P")
            ),
            /* actual = */ listOf(
                listOf(" ", "D", " "),
                listOf("N", "C", " "),
                listOf("Z", "M", "P")
            ).transpose()
        )
    }

    @Test
    fun `can transpose asymetric`() {
        assertEquals(
            /* expected = */ listOf(
                listOf(" ", "N", "Z"),
                listOf("D", "C", "M"),
                listOf(" ", " ", "P"),
                listOf(" ", "T", "Q")
            ),
            /* actual = */ listOf(
                listOf(" ", "D", " ", " "),
                listOf("N", "C", " ", "T"),
                listOf("Z", "M", "P", "Q")
            ).transpose()
        )
    }

}