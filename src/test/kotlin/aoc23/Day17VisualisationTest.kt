package aoc23

import aoc23.Day17Parser.toCity
import aoc23.Day17Solution.fourToTenStepsDirectionPredicate
import aoc23.Day17Solution.threeStepsDirectionPredicate
import common.Input.readInput
import common.Input.readInputExample
import common.Monitoring
import common.Space2D.Parser.toPointToChars
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import visualisation.animate
import kotlin.test.assertEquals

@Tag("slow")
class Day17VisualisationTest {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example last`() {
            framesFor(Day17.readInputExample(), threeStepsDirectionPredicate)
                .animate()
        }

        @Test
        fun `part one example visualisation`() {
            val expected =
                """
            ***34****1323
            32****35*5623
            32552456***54
            3446585845*52
            4546657867**6
            14385987984*4
            44578769877*6
            36378779796**
            465496798688*
            456467998645*
            12246868655**
            25465488877*5
            43226746555**
        """.trimIndent()

            val actual = framesFor(Day17.readInputExample(), threeStepsDirectionPredicate).last().format()

            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example last`() {
            framesFor(Day17.readInputExample(), fourToTenStepsDirectionPredicate)
                .animate()
        }

        @Test
        fun `part one example visualisation`() {
            val expected =
                """
            *********1323
            32154535*5623
            32552456*4254
            34465858*5452
            45466578*****
            143859879845*
            445787698776*
            363787797965*
            465496798688*
            456467998645*
            122468686556*
            254654888773*
            432267465553*
        """.trimIndent()

            val actual = framesFor(Day17.readInputExample(), fourToTenStepsDirectionPredicate).last().format()

            assertEquals(expected, actual)
        }
    }
}

private fun framesFor(input: List<String>, directionPredicate: DirectionPredicate): List<String> =
    Monitoring.PointCharMonitor(canvas = input.toPointToChars().toMap(), highlightWith = "*")
        .apply { input.toCity(this).leastHeatLoss(directionPredicate) }
        .toLoggable()