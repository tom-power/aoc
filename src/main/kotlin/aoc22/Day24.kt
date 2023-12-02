package aoc22

import aoc22.Day24Domain.Blizzard
import aoc22.Day24Domain.Clear
import aoc22.Day24Domain.Expedition
import aoc22.Day24Domain.Valley
import aoc22.Day24Domain.ValleyLocation
import aoc22.Day24Domain.Wall
import aoc22.Day24Monitoring.ValleyLocationMonitor
import aoc22.Day24Parser.toValley
import aoc22.Day24Solution.part1Day24
import aoc22.Day24Solution.part2Day24
import common.Space2D.Direction
import common.Space2D.Parser.PointChar
import common.Space2D.Parser.parsePointChars2
import common.Space2D.Point
import common.Space2D.toRange
import common.Year22
import common.Monitoring
import common.Space2D

object Day24: Year22() {
    fun List<String>.part1(): Int = part1Day24()

    fun List<String>.part2(): Int = part2Day24()
}

object Day24Solution {
    fun List<String>.part1Day24(): Int =
        toValley().run {
            shortestPath(
                begin = start,
                isDestination = { this == end },
            )
        }

    fun List<String>.part2Day24(): Int =
        toValley().run {
            val out = shortestPath(begin = start, isDestination = { this == end })
            val back = shortestPath(begin = end, isDestination = { this == start })
            val outAgain = shortestPath(begin = start, isDestination = { this == end })
            out + back + outAgain
        }
}

object Day24Domain {

    sealed interface ValleyLocation {
        val point: Point
    }

    data class Wall(override val point: Point) : ValleyLocation

    sealed interface Floor : ValleyLocation

    data class Clear(override val point: Point) : Floor
    data class Blizzard(override val point: Point, val direction: Direction) : Floor
    data class Expedition(override val point: Point) : Floor

    data class ClearCost(val clear: Clear, val cost: Int) {
        fun bump(): ClearCost = this.copy(cost = this.cost + 1)
    }

    class Valley(
        private var locations: Set<ValleyLocation>,
        val start: Clear,
        val end: Clear,
        private val visited: MutableSet<ClearCost> = mutableSetOf(),
        private val toVisit: MutableList<ClearCost> = mutableListOf(),
        private val monitor: ValleyLocationMonitor? = null
    ) {
        private val blizzardSwirls = BlizzardSwirls()

        fun shortestPath(begin: Clear, isDestination: Clear.() -> Boolean): Int =
            ShortestPath(begin, isDestination).invoke()

        private inner class ShortestPath(
            private val begin: Clear,
            private val isDestination: Clear.() -> Boolean,
        ) : () -> Int {
            override fun invoke(): Int {
                toVisit.clear()
                visited.clear()
                toVisit.add(ClearCost(begin, 0))
                while (true) {
                    locations = blizzardSwirls(locations).also { monitor?.invoke(it) }
                    val thisToVisit = toVisit.toList().also { toVisit.clear() }
                    thisToVisit.mapNotNull { it.explore() }.map { return it }
                }
            }

            private fun ClearCost.explore(): Int? =
                if (shouldVisit(this)) {
                    moves().let { moves ->
                        moves
                            .firstOrNull { it.clear.isDestination() }?.cost
                            .also {
                                toVisit.addAll(moves)
                                visited.add(this)
                            }
                    }
                } else null

            private fun shouldVisit(next: ClearCost): Boolean = next !in visited
        }

        private inner class BlizzardSwirls : (Set<ValleyLocation>) -> Set<ValleyLocation> {

            private val wallSet = locations.filterIsInstance<Wall>().toSet()
            private val wallPoints = wallSet.map(Wall::point).toSet()
            private val floorPoints = locations.filterIsInstance<Floor>().map(Floor::point).toSet()

            override fun invoke(valleyLocations: Set<ValleyLocation>): Set<ValleyLocation> =
                valleyLocations
                    .map { it.moveBlizzard() }
                    .updateClear()
                    .toSet()

            private fun ValleyLocation.moveBlizzard(): ValleyLocation =
                when (this) {
                    is Blizzard -> Blizzard(point.move(direction), direction).withWallJump()
                    else -> this
                }

            private fun Blizzard.withWallJump(): Blizzard =
                when (this.point) {
                    in wallPoints -> Blizzard(this.wallJumpPoint(), this.direction)
                    else -> this
                }

            private fun Blizzard.wallJumpPoint(): Point =
                when (this.direction) {
                    Direction.Up -> this.point.copy(y = wallPoints.minBy { it.y }.y + 1)
                    Direction.Right -> this.point.copy(x = wallPoints.minBy { it.x }.x + 1)
                    Direction.Down -> this.point.copy(y = wallPoints.maxBy { it.y }.y - 1)
                    Direction.Left -> this.point.copy(x = wallPoints.maxBy { it.x }.x - 1)
                }

            private fun List<ValleyLocation>.updateClear(): Set<ValleyLocation> {
                val blizzard = filterIsInstance<Blizzard>().toSet()
                val clear = (floorPoints - blizzard.map(Blizzard::point).toSet()).map(::Clear).toSet()
                return blizzard + clear + wallSet
            }
        }

        private fun ClearCost.moves(): List<ClearCost> =
            locations
                .filterIsInstance<Clear>()
                .filter { it.point in (clear.point.adjacent() + listOf(clear.point)) }
                .map { this.copy(clear = it).bump() }
    }
}

object Day24Parser {
    private fun List<PointChar>.toValleyLocations(): List<ValleyLocation> =
        this.map {
            when (it.char) {
                '.' -> Clear(it.point)
                '#' -> Wall(it.point)
                else -> Blizzard(it.point, it.char.toDirection())
            }
        }

    private fun Char.toDirection(): Direction =
        when (this) {
            '>' -> Direction.Right
            '^' -> Direction.Up
            '<' -> Direction.Left
            'v' -> Direction.Down
            else -> error("argh")
        }

    fun List<String>.toValley(monitor: ValleyLocationMonitor? = null): Valley =
        parsePointChars2().let { pc ->
            val locations = pc.toValleyLocations().toSet()
            Valley(
                locations = locations.toSet(),
                start = locations.filterIsInstance<Clear>().maxBy { it.point.y },
                end = locations.filterIsInstance<Clear>().minBy { it.point.y },
                monitor = monitor
            )
        }
}

object Day24Monitoring {
    class ValleyLocationMonitor : Monitoring.Monitor<Set<ValleyLocation>> {
        private val listOfVls: MutableList<Set<ValleyLocation>> = mutableListOf()

        override fun invoke(vls: Set<ValleyLocation>) {
            this.listOfVls.add(vls)
        }

        override fun print(): List<String> = listOfVls.map { it.print() }
    }

    context(Collection<ValleyLocation>)
    private fun ValleyLocation.draw(): String =
        when (this) {
            is Day24Domain.Floor -> when (this) {
                is Blizzard -> when (this.direction) {
                    Direction.Right -> ">"
                    Direction.Down -> "V"
                    Direction.Left -> "<"
                    Direction.Up -> "^"
                }

                is Clear -> "."
                is Expedition -> "E"
            }

            is Wall -> "#"
        }

    private fun Collection<ValleyLocation>.print(): String {
        val expedition = this.singleOrNull { it is Expedition }
        return with(this.map { it.point }) {
            Space2D.Axis.Y.toRange().reversed().map { y ->
                Space2D.Axis.X.toRange().map { x ->
                    val point = Point(x, y)
                    this@print
                        .firstOrNull { it.point == point }
                }
                    .filterNot { it?.point == expedition?.point }
                    .toMutableList().apply { add(expedition) }
                    .filterNotNull()
                    .map { it.draw() }
                    .joinToString("")
            }.joinToString(System.lineSeparator())
        }
    }

}
