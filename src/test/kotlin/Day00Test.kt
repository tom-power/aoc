import Day00.part1
import Day00.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day00Test {

    @Test
    @Disabled
    fun `is ok`() {
        assertEquals(Day00.readDaysInputTest().part1(), 0)
        assertEquals(Day00.readDaysInputTest().part2(), 0)
        assertEquals(Day00.readDaysInput().part1(), 0)
        assertEquals(Day00.readDaysInput().part2(), 0)
    }
}