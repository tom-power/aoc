package aoc22

import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.spreadOut
import aoc22.Day23Solution.part1Day23
import aoc22.Day23Solution.part2Day23
import aoc22.Misc.log
import aoc22.Space2D.Direction
import aoc22.Space2D.Direction.*
import aoc22.Space2D.Parser.parsePointChars
import aoc22.Space2D.Point
import aoc22.Space2D.toMaxPoints

object Day23 : Day {
    fun List<String>.part1(): Int = part1Day23()

    fun List<String>.part2(): Int = part2Day23()
}

object Day23Solution {
    fun List<String>.part1Day23(): Int =
        toElves()
            .spreadOut().invoke(rounds = 10)
            .checkProgress()

    fun List<String>.part2Day23(): Int =
        map { it.split(",") }.log()
            .let { 0 }

    private fun Elves.checkProgress(): Int = toMaxPoints().size - size
}

object Day23Runner {
    class SpreadOut(
        private val elves: Elves,
        val monitor: Monitor<Elves>? = null
    ) : (Int) -> Elves {
        private var startingDirection: Direction = Up
        private val directions: List<Direction> = listOf(Up, Down, Left, Right)

        override fun invoke(rounds: Int): Elves =
            (0 until rounds).fold(elves) { accElves: Elves, _ ->
                val moves: List<Move> =
                    accElves
                        .filter { elf -> elf.hasNeighboursIn(accElves) }
                        .mapNotNull { elf ->
                            elf.proposedDirection(elves = accElves)?.let { direction ->
                                Move(from = elf, to = elf.move(direction, by = 1))
                            }
                        }
                        .filterIsAlone()

                ((accElves - moves.map { it.from }.toSet()) + moves.map { it.to })
                    .also { nextStartingDirection() }
                    .also { monitor?.invoke(it) }
            }

        data class Move(val from: Elf, val to: Elf)

        private fun nextStartingDirection() {
            startingDirection = directions[(directions.indexOf(startingDirection) + 1) % directions.size]
        }

        private fun List<Move>.filterIsAlone(): List<Move> =
            this
                .groupBy { it.to }
                .filterNot { it.value.size > 1 }
                .flatMap { it.value }

        private fun Elf.hasNeighboursIn(elves: Set<Elf>): Boolean =
            this.adjacentWithDiagonal()
                .any { point -> point in elves }

        private fun Elf.proposedDirection(elves: Set<Elf>): Direction? =
            directions
                .startingAt(startingDirection)
                .firstOrNull { direction ->
                    pointsIn(direction).all { it !in elves }
                }

        private fun Point.pointsIn(direction: Direction): List<Point> =
            this.move(direction).let { firstMove ->
                listOf(firstMove) +
                    nextDirections(direction).map { nextDirection ->
                        firstMove.move(nextDirection)
                    }
            }

        private fun nextDirections(direction: Direction) =
            when (direction) {
                Right, Left -> listOf(Up, Down)
                Up, Down -> listOf(Left, Right)
            }

        private fun List<Direction>.startingAt(direction: Direction): List<Direction> {
            val offset = this.indexOf(direction)
            return this.foldIndexed(listOf()) { index, acc, _ ->
                this.getOrNull((index + offset) % this@startingAt.size)?.let { acc + it } ?: acc
            }
        }
    }

    fun Elves.spreadOut(monitor: Monitor<Elves>? = null): SpreadOut = SpreadOut(elves = this, monitor)
}

typealias Elves = Set<Elf>

typealias Elf = Point

object Day23Parser {
    fun List<String>.toElves(): Elves =
        parsePointChars()
            .filter { it.second == '#' }
            .map { (point, _) -> point }
            .toSet()
}
