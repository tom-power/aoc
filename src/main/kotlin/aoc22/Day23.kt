package aoc22

import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.SpreadOut
import aoc22.Day23Solution.part1Day23
import aoc22.Day23Solution.part2Day23
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
        SpreadOut(elves = toElves())
            .until(round = 10)
            .elves
            .checkProgress()

    fun List<String>.part2Day23(): Int =
        SpreadOut(elves = toElves(), stopAtNoMove = true)
            .until(round = Int.MAX_VALUE)
            .round

    private fun Elves.checkProgress(): Int = toMaxPoints().size - size
}

object Day23Runner {
    class SpreadOut(
        private val elves: Elves,
        private val stopAtNoMove: Boolean = false,
        private val monitor: Monitor<Elves>? = null,
    ) {
        private var startingDirection: Direction = Up
        private val directions: List<Direction> = listOf(Up, Down, Left, Right)

        fun until(round: Int): State =
            (1..round).fold(State(elves, 1)) { acc, index ->
                acc.elves.move()
                    .let { next -> State(next, index) }
                    .also { next -> if (isNoMove(acc, next) && stopAtNoMove) return next }
                    .also { monitor?.invoke(acc.elves) }
            }

        private fun isNoMove(acc: State, next: State) = acc.elves == next.elves

        data class State(val elves: Elves, val round: Int)

        private fun Elves.move(): Elves =
            FindMoves(this).invoke().let { moves ->
                this.moveUsing(moves)
                    .also { nextStartingDirection() }
            }

        inner class FindMoves(private val elves: Elves) : () -> List<Move> {
            override fun invoke(): List<Move> =
                elves
                    .filter { elf -> elf.hasNeighboursIn(elves) }
                    .mapNotNull { elf ->
                        elf.run {
                            proposedDirection(elves = elves)?.let { toMove(it) }
                        }
                    }
                    .filterIsAlone()

            private fun Elf.toMove(direction: Direction): Move =
                Move(from = this, to = this.move(direction, by = 1))

            private fun List<Move>.filterIsAlone(): List<Move> =
                this
                    .groupBy { it.to }
                    .filter { it.value.size == 1 }
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
                    listOf(firstMove) + perpendicularDirections(direction).map { firstMove.move(it) }
                }

            private fun perpendicularDirections(direction: Direction): List<Direction> =
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

        private fun Elves.moveUsing(moves: List<Move>): Elves {
            val stayed = this - moves.map { it.from }.toSet()
            val moved = moves.map { it.to }
            return stayed + moved
        }

        data class Move(val from: Elf, val to: Elf)

        private fun nextStartingDirection() {
            startingDirection = directions[(directions.indexOf(startingDirection) + 1) % directions.size]
        }

    }
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
