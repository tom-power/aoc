package aoc22

import aoc22.Collections.partitionedBy
import aoc22.Day22DirectionUtils.toScore
import aoc22.Day22DirectionUtils.turn
import aoc22.Day22Domain.Step
import aoc22.Day22Domain.Board
import aoc22.Day22Domain.Side
import aoc22.Day22Domain.Side.L
import aoc22.Day22Domain.Side.R
import aoc22.Day22Domain.Tile
import aoc22.Day22Domain.Tiles
import aoc22.Day22Domain.Turn
import aoc22.Day22Domain.Wall
import aoc22.Day22Parser.toPath
import aoc22.Day22Parser.toBoard
import aoc22.Day22PointUtils.otherSide
import aoc22.Day22Solution.part1Day22
import aoc22.Day22Solution.part2Day22
import aoc22.Misc.log
import aoc22.Space2D.Direction
import aoc22.Space2D.Direction.*
import aoc22.Space2D.Point
import kotlin.math.absoluteValue

object Day22 : Day {
    fun List<String>.part1(): Int = part1Day22()

    fun List<String>.part2(): Int = part2Day22()
}

object Day22Solution {
    fun List<String>.part1Day22(): Int =
        with(toBoard()) {
            follow(toPath()).password()
//                .also { this.monitor.print() }
        }

    fun List<String>.part2Day22(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

object Day22Domain {
    class Board(
        private val items: Set<BoardItem>,
        val monitor: Monitor = Monitor(items),
    ) {
        private val tiles: Set<Tile> = items.filterIsInstance<Tile>().toSet()
        private val points: Set<Point> = items.map { it.point }.toSet()

        fun follow(path: List<Step>): State =
            path.fold(initialState()) { last, step ->
                last.move(step)
                    .also { monitor(it, step) }
            }

        private fun State.move(step: Step): State =
            when (step) {
                is Tiles -> this.copy(point = this.point.moveOverTiles(this.facing, step.tiles))
                is Turn -> this.copy(facing = this.facing.turn(step.side))
            }

        private fun Point.moveOverTiles(direction: Direction, by: Int): Point =
            IntArray(by) { 1 }.fold(this) { last, byStep ->
                nextBoardItemFor(last, direction, byStep).let { next ->
                    when (next) {
                        is Tile -> next.point
                        is Wall -> return last
                    }
                }
            }

        private fun nextBoardItemFor(last: Point, direction: Direction, by: Int) =
            boardItem(last.move(direction, by))
                ?: boardItem(points.otherSide(last, direction))!!

        private fun boardItem(point: Point): BoardItem? = items.singleOrNull { it.point == point }

        private fun initialState(): State = State(topLeftTile().point, Right)

        private fun topLeftTile(): Tile {
            val minY = tiles.maxOf { it.point.y }
            return tiles.filter { it.point.y == minY }.minBy { it.point.x }
        }
    }

    enum class Side { R, L }

    sealed interface Step
    data class Tiles(val tiles: Int) : Step
    data class Turn(val side: Side) : Step

    sealed interface BoardItem {
        val point: Point
    }

    class Tile(override val point: Point) : BoardItem
    class Wall(override val point: Point) : BoardItem

    data class State(
        val point: Point,
        val facing: Direction
    ) {
        fun password(): Int =
            (1000 * (point.y.absoluteValue + 1)) + (4 * (point.x.absoluteValue + 1)) + facing.toScore()
    }

}

object Day22PointUtils {
    fun Set<Point>.otherSide(point: Point, direction: Direction): Point =
        when (direction) {
            Right -> rowFor(point).minBy { it.x }
            Down -> colFor(point).maxBy { it.y }
            Left -> rowFor(point).maxBy { it.x }
            Up -> colFor(point).minBy { it.y }
        }

    private fun Set<Point>.rowFor(point: Point): List<Point> = filter { it.y == point.y }
    private fun Set<Point>.colFor(point: Point): List<Point> = filter { it.x == point.x }
}

object Day22DirectionUtils {
    private val clockWiseDirections = listOf(Right, Down, Left, Up)

    fun Direction.toScore(): Int = clockWiseDirections.indexOf(this)

    fun Direction.turn(side: Side): Direction =
        clockWiseDirections.let {
            val i = it.indexOf(this) + side.toInt()
            it[(i % it.size + it.size) % it.size]
        }

    private fun Side.toInt(): Int =
        when (this) {
            R -> +1
            L -> -1
        }
}

object Day22Parser {
    fun List<String>.toBoard(): Board =
        partitionedBy("")[0]
            .flatMapIndexed { y, row ->
                val first = row.indexOfFirst { it.isBoardItem() }
                val last = row.indexOfLast { it.isBoardItem() }
                (first..last).map { x ->
                    Point(x, y).let {
                        when (row[x]) {
                            '.' -> Tile(it.toBoardPoint())
                            '#' -> Wall(it.toBoardPoint())
                            else -> error("board item not found")
                        }
                    }
                }
            }.let { Board(it.toSet()) }

    private fun Point.toBoardPoint() = Point(x, -y)

    private fun Char.isBoardItem(): Boolean = this == '.' || this == '#'

    fun List<String>.toPath(): List<Step> =
        partitionedBy("")[1][0]
            .fold(mutableListOf<String>()) { acc, c ->
                val last = acc.lastOrNull()
                when {
                    last != null && last.isDigit() && c.isDigit() ->
                        acc.dropLast(1).toMutableList()
                            .apply { add(last + c.toString()) }

                    else -> acc.apply { add(c.toString()) }
                }
            }.map {
                when {
                    it.isDigit() -> Tiles(it.toInt())
                    else -> Turn(Side.valueOf(it))
                }
            }

    private fun String.isDigit() = all { it.isDigit() }
}