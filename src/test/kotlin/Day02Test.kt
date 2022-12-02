import Day02.part1
import Day02.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun `is ok`() {
        assertEquals(Day02.readDaysInputTest().part1(), 15)
        assertEquals(Day02.readDaysInputTest().part2(), 12)
        assertEquals(Day02.readDaysInput().part1(), 13009)
        assertEquals(Day02.readDaysInput().part2(), 10398)
    }
}