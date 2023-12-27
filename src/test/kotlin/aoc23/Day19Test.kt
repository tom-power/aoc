package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day19.part1
import aoc23.Day19.part2
import aoc23.Day19Domain.Rating
import aoc23.Day19Domain.RatingCategory
import aoc23.Day19Domain.RatingCategory.*
import aoc23.Day19Parser.toAplenty
import com.github.shiguruikai.combinatoricskt.permutations
import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition
import common.Misc.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day19Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(19114, Day19.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(398527, Day19.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(167409079868000, Day19.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(133973513090020, Day19.readInput().part2())
        }
    }

    @Nested
    inner class PartTwoExamples {
        @Test
        fun `part one example 1 single`() {
            assertEquals(256, Day19.readInput("_example1").part2(overrideParts = singleParts()))
        }

        @Test
        fun `part one example 1 range`() {
            assertEquals(256, Day19.readInput("_example1").part2(overrideParts = rangeParts()))
        }
    }
}

private val oneToFour = IntRange(1, 4)

private fun singleParts(): List<Day19Domain.Part> =
    oneToFour.permutationsWithRepetition(4).toList().map {
        Day19Domain.Part(
            ratings = listOf(
                Rating(x, IntRange(it[0], it[0])),
                Rating(m, IntRange(it[1], it[1])),
                Rating(a, IntRange(it[2], it[2])),
                Rating(s, IntRange(it[3], it[3])),
            )
        )
    }

private fun rangeParts(): List<Day19Domain.Part> =
    listOf(
        Day19Domain.Part(
            ratings =
            RatingCategory.entries.map { ratingCategory ->
                Rating(ratingCategory, oneToFour)
            }
        )
    )