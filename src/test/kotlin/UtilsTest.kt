import aoc22.Collections.partitionedBy
import aoc22.Collections.transpose
import aoc22.Matrix
import aoc22.Matrix.Point
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

    @Test
    fun `test partitioned`() {
        val expected = listOf(listOf("hi"), listOf("foo", "bar"), listOf("baz"))
        val actual = listOf("hi", "", "foo", "bar", "", "baz").partitionedBy("")

        assertEquals(expected, actual)
    }

    @Test
    fun `test getAdjacent`() {
        val expected = setOf(
            Point(x = 1, y = 0),
            Point(x = 0, y = -1),
            Point(x = -1, y = 0),
            Point(x = 0, y = 1),
        )
        val actual = Point(0, 0).getAdjacent()

        assertEquals(expected, actual)
    }

    @Test
    fun `test getAdjacentWithDiagonal`() {
        val expected = setOf(
            Point(x = 1, y = 0),
            Point(x = 1, y = -1),
            Point(x = 0, y = -1),
            Point(x = -1, y = -1),
            Point(x = -1, y = 0),
            Point(x = -1, y = 1),
            Point(x = 0, y = 1),
            Point(x = 1, y = 1)
        )
        val actual = Point(0, 0).getAdjacentWithDiagonal()

        assertEquals(expected, actual)
    }
}