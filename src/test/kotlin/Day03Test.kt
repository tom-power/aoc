import Day03.part1
import Day03.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day03Test {

    @Test
    fun `is ok`() {
        assertEquals(Day03.readDaysInputTest().part1(), 157)
        assertEquals(Day03.readDaysInputTest().part2(), 70)
        assertEquals(Day03.readDaysInput().part1(), 7817)
        assertEquals(Day03.readDaysInput().part2(), 2444)
    }
}