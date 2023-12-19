package aoc22

import common.Collections.partitionedBy
import aoc22.Day22Domain.Board
import aoc22.Day22Domain.BoardItem
import aoc22.Day22Domain.State
import aoc22.Day22Domain.Step
import aoc22.Day22Domain.Tile
import aoc22.Day22Domain.Tiles
import aoc22.Day22Domain.Turn
import aoc22.Day22Domain.Wall
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import aoc22.Day22Parser.toPoints
import aoc22.Day22DomainWrapCube.toEdgePointMap
import aoc22.Day22UtilsPoint.oppositeFor
import aoc22.Day22DomainWrap.withCubeWrap
import aoc22.Day22DomainWrap.withFlatWrap
import aoc22.Day22Solution.part1Day22
import aoc22.Day22Solution.part2Day22
import common.Space2D
import common.Space2D.Direction
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Space2D.Side
import common.Space2D.opposite
import common.Space2D.turn
import common.Year22
import kotlin.math.absoluteValue

object Day22: Year22 {
    fun List<String>.part1(): Int = part1Day22()

    fun List<String>.part2(): Int = part2Day22()
}

object Day22Solution {
    fun List<String>.part1Day22(): Int = toBoard().withFlatWrap().password()

    fun List<String>.part2Day22(): Int = toBoard().withCubeWrap().password()

    context(List<String>)
    private fun Board.password(): Int = follow(toPath()).password()

    private fun State.password(): Int =
        ((1000 * (point.y.absoluteValue + 1))
            + (4 * (point.x.absoluteValue + 1))
            + facing.toScore())

    fun Direction.toScore(): Int = Space2D.clockWiseDirections.indexOf(this)
}

object Day22Domain {
    data class Board(
        val items: Set<BoardItem>,
        val wrap: Wrap,
        val monitor: StateMonitor = StateMonitor(items),
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
                is Tiles ->
                    this.moveOverTiles(by = step.tiles)
                        .let { (point, direction) ->
                            this.copy(point = point, facing = direction)
                        }

                is Turn -> this.copy(facing = this.facing.turn(step.side))
            }

        private fun State.moveOverTiles(by: Int): State =
            IntArray(by) { 1 }.fold(this) { last, byStep ->
                nextBoardItemFor(last, byStep).let { (nextBoardItem, nextDirection) ->
                    when (nextBoardItem) {
                        is Tile -> State(nextBoardItem.point, nextDirection)
                        is Wall -> return last
                    }
                }
            }

        private fun nextBoardItemFor(last: State, by: Int): Pair<BoardItem, Direction> =
            boardItem(last.point.move(last.facing, by))
                ?.let { Pair(it, last.facing) }
                ?: wrap(last).let { Pair(boardItem(it.point)!!, it.facing) }

        private fun boardItem(point: Point): BoardItem? = items.singleOrNull { it.point == point }

        private fun initialState(): State = State(topLeftTile().point, East)

        private fun topLeftTile(): Tile {
            val minY = tiles.maxOf { it.point.y }
            return tiles.filter { it.point.y == minY }.minBy { it.point.x }
        }
    }

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
    )
}

typealias Wrap = (State) -> State

data class EdgePoint(val point: Point, val directionOnEnter: Direction)
typealias EdgePointPair = Pair<EdgePoint, EdgePoint>

object Day22DomainWrap {
    class FlatWrap(
        private val points: Set<Point>
    ) : Wrap {
        override fun invoke(state: State): State =
            State(
                point = points.oppositeFor(state.point, state.facing),
                facing = state.facing
            )
    }

    fun Board.withFlatWrap(): Board = copy(wrap = FlatWrap(this.items.toPoints()))

    class CubeWrap(
        val edgePointPairs: Set<EdgePointPair>
    ) : Wrap {
        override fun invoke(state: State): State =
            edgePointPairs
                .run {
                    mapFor(state, from = { it.first }, dest = { it.second })
                        ?: mapFor(state, from = { it.second }, dest = { it.first })
                }!!
                .toState()

        private fun Set<EdgePointPair>.mapFor(
            state: State,
            from: (EdgePointPair) -> EdgePoint,
            dest: (EdgePointPair) -> EdgePoint
        ): EdgePoint? =
            this.singleOrNull {
                from(it).point == state.point
                    && from(it).directionOnEnter == state.facing.opposite()
            }?.let(dest)

        private fun EdgePoint.toState(): State = State(point = point, facing = directionOnEnter)
    }

    fun Board.withCubeWrap(): Board = copy(wrap = CubeWrap(this.items.toPoints().toEdgePointMap()))
}

object Day22DomainWrapCube {
    fun Set<Point>.toEdgePointMap(): Set<EdgePointPair> =
        EdgePointMap(
            edgePoints = EdgePoints(this).invoke()
        ).invoke()

    class EdgePoints(private val points: Set<Point>) : () -> Set<EdgePoint> {
        override fun invoke(): Set<EdgePoint> = points.colEdgePoints() + points.rowEdgePoints()

        private fun Set<Point>.colEdgePoints(): Set<EdgePoint> =
            edgePointsFor(
                groupBy = { it.x },
                minMaxBy = { it.y },
                minDirection = North,
                maxDirection = South
            )

        private fun Set<Point>.rowEdgePoints(): Set<EdgePoint> =
            edgePointsFor(
                groupBy = { it.y },
                minMaxBy = { it.x },
                minDirection = East,
                maxDirection = West
            )

        private fun Set<Point>.edgePointsFor(
            groupBy: (Point) -> Int,
            minMaxBy: (Point) -> Int,
            minDirection: Direction,
            maxDirection: Direction
        ): Set<EdgePoint> =
            groupBy { groupBy(it) }.values
                .flatMap { points ->
                    setOf(
                        EdgePoint(points.minBy { minMaxBy(it) }, minDirection),
                        EdgePoint(points.maxBy { minMaxBy(it) }, maxDirection)
                    )
                }
                .toSet()
    }

    class EdgePointMap(
        private val edgePoints: Set<EdgePoint>,
        val monitor: EdgePointMapMonitor? = null
    ) : () -> Set<EdgePointPair> {
        private val edges = edgePoints.map { it.point }.toSet()

        private val initialPairs: List<EdgePointPair> =
            edgePoints
                .groupBy { it.point.findMissingInnerCorner() }
                .filterKeys { it != null }
                .values
                .map { it.run { this[0] to this[1] } }

        private fun Point.findMissingInnerCorner(): Point? =
            edges.adjacentFor(this).singleOrNull()
                ?.let { this.otherSideFrom(it) }

        private fun Point.otherSideFrom(point: Point): Point = this.move(point.directionTo(this), 1)

        private val edgePointMap: MutableList<EdgePointPair> = mutableListOf()

        override fun invoke(): Set<EdgePointPair> =
            initialPairs.map { initial ->
                var last: EdgePointPair = initial
                var next: EdgePointPair? = initial
                while (next != null) {
                    edgePointMap.add(last)
                    next = last.getNext()?.also { last = it }
                        .also { monitor?.let { it.invoke(edgePointMap.toSet()) } }
                }
                edgePointMap
            }.flatten().toSet()

        private fun EdgePointPair.getNext(): EdgePointPair? =
            first.nextEdgePoint()?.let { first ->
                second.nextEdgePoint()?.let { second ->
                    Pair(
                        first = first,
                        second = second
                    )
                }
            }?.takeUnless { isOuterCorner(it.first.point) && isOuterCorner(it.second.point) }

        private fun EdgePoint.nextEdgePoint(): EdgePoint? =
            (edges.adjacentFor(point) + setOf(point))
                .flatMap { it.toEdgePoints() }
                .filter { it !in edgePointMap.edgePoints() }
                .let { it.singleOrNull() ?: it.outerCornerFor(this) }

        private fun List<EdgePoint>.outerCornerFor(last: EdgePoint): EdgePoint? =
            firstOrNull { it.directionOnEnter == last.directionOnEnter }
                ?: lastOrNull { it.directionOnEnter != last.directionOnEnter }

        private fun Point.toEdgePoints(): List<EdgePoint> =
            edgePoints
                .filter { it.point == this }

        private fun List<EdgePointPair>.edgePoints(): Set<EdgePoint> =
            this.flatMap { listOf(it.first, it.second) }.toSet()

        private fun isOuterCorner(point: Point): Boolean {
            fun Set<Point>.isStraight(): Boolean =
                this.maxOf { it.x } == this.minOf { it.x } || this.maxOf { it.y } == this.minOf { it.y }

            return !edges.adjacentFor(point).isStraight()
        }
    }

    private fun Set<Point>.adjacentFor(point: Point): Set<Point> =
        point.adjacent().filter { it in this }.toSet()
}

object Day22UtilsPoint {
    fun Set<Point>.oppositeFor(point: Point, direction: Direction): Point =
        when (direction) {
            East -> rowFor(point).minBy { it.x }
            South -> colFor(point).maxBy { it.y }
            West -> rowFor(point).maxBy { it.x }
            North -> colFor(point).minBy { it.y }
        }

    private fun Set<Point>.rowFor(point: Point): List<Point> = filter { it.y == point.y }
    private fun Set<Point>.colFor(point: Point): List<Point> = filter { it.x == point.x }
}

object Day22Parser {
    fun List<String>.toBoard(): Board {
        val items = toBoardItems().toSet()
        return Board(items = items, wrap = { error("don't call me") })
    }

    private fun List<String>.toBoardItems(): Set<BoardItem> =
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
            }.toSet()

    private fun Point.toBoardPoint() = Point(x, -y)

    private fun Char.isBoardItem(): Boolean = this == '.' || this == '#'

    fun Set<BoardItem>.toPoints(): Set<Point> = map { it.point }.toSet()

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

