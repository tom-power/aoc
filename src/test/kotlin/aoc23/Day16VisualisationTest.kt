package aoc23

import aoc23.Day16Parser.toContraption
import common.Input.readInput
import common.Input.readInputExample
import common.Monitoring.PointCharMonitor
import common.Space2D
import common.Space2D.Parser.toPointToChars
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import visualisation.animate
import kotlin.test.assertEquals

@Tag("visualisation")
class Day16VisualisationTest {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example animation`() {
            framesFor(input = Day16.readInputExample()) { this.beamFromTopLeft() }
                .animate()
        }

        @Test
        fun `part one animation`() {
            framesFor(input = Day16.readInput()) { this.beamFromTopLeft() }
                .animate()
        }

        @Test
        fun `part one example visualisation`() {
            val expected =
                """
            ######....
            .#...#....
            .#...#####
            .#...##...
            .#...##...
            .#...##...
            .#..####..
            ########..
            .#######..
            .#...#.#..
        """.trimIndent()

            val actual = framesFor(input = Day16.readInputExample()) { this.beamFromTopLeft() }.last().format()

            assertEquals(expected, actual)
        }

        @Test
        fun `part one corner start`() {
            val expected =  """
            ####
            #...
            #...
            #...
            #...
        """.trimIndent()

            val input = """
            /...
            ....
            ....
            ....
            \../
        """.trimIndent()

            val actual =
                framesFor(input = input.split(System.lineSeparator())) {
                    this.beamFrom(
                        Day16Domain.PointDir(
                            point = Space2D.Point(x = 0, y = 1),
                            direction = Space2D.Direction.North
                        )
                    )
                }.last().format()

            assertEquals(expected, actual)
        }

        @Test
        fun `part one loops`() {
            val expected =  """
            ####
            #..#
            #..#
            #..#
            ####
        """.trimIndent()

            val input = """
            /..\
            ....
            ....
            ....
            \../
        """.trimIndent()

            val actual =
                framesFor(input = input.split(System.lineSeparator())) {
                    this.beamFrom(
                        Day16Domain.PointDir(
                            point = Space2D.Point(x = 0, y = 2),
                            direction = Space2D.Direction.North
                        )
                    )
                }.last().format()

            assertEquals(expected, actual)
        }

        @Test
        fun `part one edges`() {
            val expected =  """
            ####
            #..#
            #..#
            #..#
            #..#
        """.trimIndent()

            val input = """
            /..\
            ....
            ....
            ....
            -..|
        """.trimIndent()

            val actual =
                framesFor(input = input.split(System.lineSeparator())) {
                    this.beamFrom(
                        Day16Domain.PointDir(
                            point = Space2D.Point(x = 0, y = 1),
                            direction = Space2D.Direction.North
                        )
                    )
                }.last().format()

            assertEquals(expected, actual)
        }

        @Test
        fun `part one T`() {
            val expected =  """
            .#.
            .#.
            .#.
            .#.
            ###
        """.trimIndent()

            val input = """
            .|.
            |.-
            ...
            ...
            .-.
        """.trimIndent()

            val actual =
                framesFor(input = input.split(System.lineSeparator())) {
                    this.beamFrom(
                        Day16Domain.PointDir(
                            point = Space2D.Point(x = 1, y = 5),
                            direction = Space2D.Direction.South
                        )
                    )
                }.last().format()

            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example animation`() {
            framesFor(input = Day16.readInputExample()) {
                this.beamFrom(
                    Day16Domain.PointDir(
                        point = Space2D.Point(x = 3, y = 10),
                        direction = Space2D.Direction.South
                    )
                )
            }
                .animate(frameDuration = 100, looping = false)
        }

        @Test
        fun `part two example visualisation single`() {
            val expected =
                """
            .#####....
            .#.#.#....
            .#.#.#####
            .#.#.##...
            .#.#.##...
            .#.#.##...
            .#.#####..
            ########..
            .#######..
            .#...#.#..
        """.trimIndent()

            val actual =
                framesFor(input = Day16.readInputExample()) {
                    this.beamFrom(
                        Day16Domain.PointDir(
                            point = Space2D.Point(x = 3, y = 10),
                            direction = Space2D.Direction.South
                        )
                    )
                }
                    .map { it.format() }
                    .maxBy { frame -> frame.filter { it == '#' }.count() }

            assertEquals(expected, actual)
        }

        @Test
        fun `part two example visualisation all`() {
            val expected =
                """
            .#####....
            .#.#.#....
            .#.#.#####
            .#.#.##...
            .#.#.##...
            .#.#.##...
            .#.#####..
            ########..
            .#######..
            .#...#.#..
        """.trimIndent()

            val actual =
                framesFor(input = Day16.readInputExample()) { beamsFromEverywhere() }
                    .map { it.format() }
                    .maxBy { frame -> frame.filter { it == '#' }.count() }

            assertEquals(expected, actual)
        }
    }
}

private fun String.format(): String =
    replace("/", ".")
        .replace("\\", ".")
        .replace("|", ".")
        .replace("-", ".")
        .replace("*", "#")

private fun framesFor(input: List<String>, fn: Day16Domain.Contraption.() -> Unit): List<String> =
    PointCharMonitor(canvas = input.toPointToChars().toMap(), highlightWith = "*")
        .apply { input.toContraption(this).fn() }
        .toLoggable()