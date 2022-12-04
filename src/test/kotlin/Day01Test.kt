import Day01.part1
import Day01.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled

class Day01Test {

    @Test
    fun `part one test`() {
        assertEquals(24000, Day01.readDaysInputTest().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(70613, Day01.readDaysInput().part1())
    }

    @Test
    fun `part two`() {
        assertEquals(70613, Day01.readDaysInput().part2())
    }
}