package aoc23

import aoc23.Day18Domain.DigPlan
import aoc23.Day18Domain.Lagoon
import aoc23.Day18Parser.toLagoon
import common.Monitoring
import common.Space2D
import common.Year23
import kotlin.math.absoluteValue

object Day18 : Year23 {
    fun List<String>.part1(): Long =
        toLagoon(planFromColours = false)
            .calculateArea()

    fun List<String>.part2(): Long =
        toLagoon(planFromColours = true)
            .calculateArea()
}

object Day18Domain {
    data class Lagoon(
        val digPlans: List<DigPlan>,
        val start: PointOfLong = PointOfLong(x = 0, y = 0),
    ) {
        private val trenchLength: Long = digPlans.sumOf { it.distance }
        private val trenchVertices: Set<PointOfLong> =
            digPlans.fold(setOf(start)) { acc, digPlan ->
                acc + acc.last().move(digPlan.direction, digPlan.distance)
            }

        fun calculateArea(): Long {
            val boundaryCount: Long = trenchLength
            val area: Long = areaFrom(trenchVertices)
            val interiorCount: Long = interiorCountFrom(area, boundaryCount)
            return (boundaryCount + interiorCount)
        }

        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        private fun interiorCountFrom(area: Long, boundary: Long): Long = area - (boundary / 2L) + 1L

        // https://en.wikipedia.org/wiki/Shoelace_formula
        private fun areaFrom(vertices: Set<PointOfLong>): Long =
            (vertices
                .zipWithNext { a, b ->
                    (a.x * b.y) - (a.y * b.x)
                }.sum() / 2).absoluteValue
    }

    data class DigPlan(
        val direction: Space2D.Direction,
        val distance: Long,
    )

    data class PointOfLong(
        val x: Long, val y: Long
    ) {
        fun move(direction: Space2D.Direction, by: Long = 1): PointOfLong =
            when (direction) {
                Space2D.Direction.North -> copy(y = y + by)
                Space2D.Direction.East -> copy(x = x + by)
                Space2D.Direction.South -> copy(y = y - by)
                Space2D.Direction.West -> copy(x = x - by)
            }
    }
}

object Day18Parser {
    fun List<String>.toLagoon(planFromColours: Boolean = false): Lagoon =
        Lagoon(
            digPlans = map {
                it.split(" ").take(3).let { (direction, distance, colour) ->
                    when {
                        planFromColours -> {
                            val instruction =
                                colour
                                    .replace("(", "")
                                    .replace(")", "")

                            DigPlan(
                                direction = instruction.toDirectionChar().toDirection(),
                                distance = instruction.toDistance(),
                            )
                        }

                        else -> {
                            DigPlan(
                                direction = direction.toDirection(),
                                distance = distance.toLong(),
                            )
                        }
                    }
                }
            },
        )

    private fun String.toDirectionChar(): String =
        when (last().digitToInt()) {
            0 -> "R"
            1 -> "D"
            2 -> "L"
            3 -> "U"
            else -> error("bad")
        }

    @OptIn(ExperimentalStdlibApi::class)
    private fun String.toDistance(): Long = drop(1).take(5).hexToLong()

    private fun String.toDirection(): Space2D.Direction =
        when (this) {
            "U" -> Space2D.Direction.North
            "D" -> Space2D.Direction.South
            "R" -> Space2D.Direction.East
            "L" -> Space2D.Direction.West
            else -> error("Unknown direction: $this")
        }
}
