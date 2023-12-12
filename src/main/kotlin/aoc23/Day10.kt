package aoc23

import aoc23.Day10Domain.Pipes
import common.Year23
import aoc23.Day10Parser.toPipes
import aoc23.Day10Solution.part1Day10
import aoc23.Day10Solution.part2Day10
import common.Space2D
import common.Space2D.Direction
import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import common.Space2D.opposite
import common.Space2D.turn

object Day10 : Year23 {
    fun List<String>.part1(): Int = part1Day10()

    fun List<String>.part2(): Int = part2Day10()
}

object Day10Solution {
    fun List<String>.part1Day10(): Int =
        toPipes()
            .stepsToFarthestPoint()

    fun List<String>.part2Day10(): Int =
        toPipes()
            .numberOfEnclosedTiles()
}

object Day10Domain {


    data class Pipes(
        private val pipeMap: MutableMap<Point, Char>
    ) {
        private lateinit var start: Point
        private lateinit var entryDirection: Direction
        private lateinit var current: Point
        private lateinit var next: Point

        private val groundPoints: MutableSet<Point> = mutableSetOf()
        private val pipePoints: MutableSet<Point> = mutableSetOf()

        private val pointCounter: PointCounter = PointCounter()

        fun stepsToFarthestPoint(): Int {
            setupStart()
            walkThePipes(checkRight = false)

            return pointCounter.stepsToFarthest()
        }

        fun numberOfEnclosedTiles(): Int {
            setupStart()
            walkThePipes(checkRight = false)
            removeTheJunk()

            setupStart()
            walkThePipes(checkRight = true)

            return pointCounter.groundInside()
        }

        private val directions: Directions = Directions(pipeMap)

        private fun setupStart() {
            start = pipeMap.filter { it.value == 'S' }.keys.first()
            entryDirection = entries.first { direction ->
                directions.entryFor(start.move(direction), direction) != null
            }
        }

        private fun removeTheJunk() {
            (pipeMap.keys - pipePoints).forEach {
                pipeMap[it] = '.'
            }
        }

        private fun walkThePipes(checkRight: Boolean) {
            current = start
            next = start.move(entryDirection)

            while (next != start) {
                walkAPipe()
                    .also { pipePoints.addAll(listOf(current, next)) }
                    .also {
                        if (checkRight) {
                            listOf(next, current).forEach {
                                checkNeighbours(it.move(entryDirection.turn(Space2D.Side.Right)))
                            }
                        }
                    }
            }
        }

        private fun walkAPipe() {
            entryDirection = directions.exitFor(next, entryDirection)!!
            current = next
            next = current.move(entryDirection)
        }

        private fun checkNeighbours(point: Point) {
            if (pipeMap[point] == '.' && point !in groundPoints) {
                groundPoints.add(point)
                Direction.entries.forEach { checkNeighbours(point.move(it)) }
            }
        }

        inner class PointCounter {
            fun stepsToFarthest(): Int = pipePoints.size / 2

            fun groundInside(): Int =
                when {
                    groundPoints.all { it.x != 0 && it.y != 0 } -> groundPoints
                    else -> groundPointsInverse
                }.size

            private val groundPointsInverse: Set<Point>
                get() = (pipeMap.keys - groundPoints - pipePoints)
        }
    }

    class Directions(
        private val pipeMap: Map<Point, Char>
    ) {
        fun entryFor(point: Point, direction: Direction) =
            directionFor(point, where = { it == direction.opposite() })

        fun exitFor(point: Point, direction: Direction): Direction? =
            directionFor(point, where = { it != direction.opposite() })

        private fun directionFor(point: Point, where: (Direction) -> Boolean): Direction? =
            directionsFor(point)
                ?.filter { where(it) }
                ?.firstOrNull()

        private fun directionsFor(point: Point): List<Direction>? =
            pipeDirectionMap[pipeMap[point]]?.toList()

        private val pipeDirectionMap: Map<Char, Pair<Direction, Direction>> =
            mapOf(
                '7' to Pair(Left, Down),
                'J' to Pair(Up, Left),
                'L' to Pair(Up, Right),
                'F' to Pair(Right, Down),
                '|' to Pair(Up, Down),
                '-' to Pair(Right, Left),
            )
    }
}

object Day10Parser {
    fun List<String>.toPipes(): Pipes =
        Pipes(
            pipeMap = toPointToChars().toMap().toMutableMap()
        )
}
