import Day01.part1
import Day01.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled

class Day01Test {

    @Test
    @Disabled
    fun `is ok`() {
        assertEquals(Day01.readDaysInputTest().part1(), 24000)
//        assertEquals(Day01.readDaysInputTest().part2(), 15)
        assertEquals(Day01.readDaysInput().part1(), 70613)
        assertEquals(Day01.readDaysInput().part2(), 205805)
    }
}