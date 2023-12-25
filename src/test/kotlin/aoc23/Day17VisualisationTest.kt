package aoc23

import aoc23.Day17Parser.toCity
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
            framesFor(input = Day17.readInputExample())
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

            val actual = framesFor(input = Day17.readInputExample()).last().format()

            assertEquals(expected, actual)
        }
    }
}

private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointCharMonitor(canvas = input.toPointToChars().toMap(), highlightWith = "*")
        .apply { input.toCity(this).leastHeatLoss() }
        .toLoggable()