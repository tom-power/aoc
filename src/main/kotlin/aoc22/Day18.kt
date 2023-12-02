package aoc22

import aoc22.Day18Domain.CubeRange
import aoc22.Day18Runner.countExposedSides
import aoc22.Day18Runner.countExposedSidesOutside
import aoc22.Day18Solution.part1Day18
import aoc22.Day18Solution.part2Day18
import aoc22.Space3D.Parser.toPoint3D
import aoc22.Space3D.Point3D
import common.Day

object Day18 : Day {
    fun List<String>.part1(): Int = part1Day18()

    fun List<String>.part2(): Int = part2Day18()
}

object Day18Solution {
    fun List<String>.part1Day18(): Int =
        map { it.toPoint3D() }
            .countExposedSides()

    fun List<String>.part2Day18(): Int =
        map { it.toPoint3D() }
            .countExposedSidesOutside()
}

object Day18Runner {
    fun List<Point3D>.countExposedSides(): Int =
        sumOf { point ->
            point.neighours().count { it !in this }
        }

    fun List<Point3D>.countExposedSidesOutside(): Int {
        val cubeRange = this.toCubeRange()
        val seen = mutableSetOf<Point3D>()

        fun countExposedOutside(point3D: Point3D): Int =
            when {
                !cubeRange.contains(point3D) || point3D in seen -> 0
                point3D in this -> 1
                else -> {
                    seen += point3D
                    point3D.neighours().sumOf { next ->
                        countExposedOutside(next)
                    }
                }
            }

        return countExposedOutside(cubeRange.firstPoint())
    }

    private fun List<Point3D>.toCubeRange() =
        CubeRange(
            x = this.toRangeWithOutside { it.x },
            y = this.toRangeWithOutside { it.y },
            z = this.toRangeWithOutside { it.z }
        )

    private fun List<Point3D>.toRangeWithOutside(fn: (Point3D) -> Int): IntRange =
        this.minOf(fn) - 1..this.maxOf(fn) + 1
}

object Day18Domain {
    class CubeRange(
        val x: IntRange,
        val y: IntRange,
        val z: IntRange,
    ) {
        fun contains(point3D: Point3D): Boolean =
            point3D.x in x && point3D.y in y && point3D.z in z

        fun firstPoint(): Point3D =
            Point3D(
                x = x.first,
                y = y.first,
                z = z.first
            )
    }
}