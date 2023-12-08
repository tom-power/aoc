package common

import common.Misc.next
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Space2D.print
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.sign

object Math {
    fun List<Long>.toLowestCommonMultiple(): Long =
        this.fold(first()) { acc, l ->
            lowestCommonMultiple(acc, l)
        }

    private fun lowestCommonMultiple(x: Long, y: Long): Long {
        val increment = maxOf(x, y)
        val maxCommonMultiple = x * y
        return generateSequence(increment) { current ->
            when {
                current >= maxCommonMultiple || current.isCommonMultipleOf(x, y) -> null
                else -> current + increment
            }
        }.last()
    }

    private fun Long.isCommonMultipleOf(x: Long, y: Long): Boolean = this % x == 0L && this % y == 0L
}

object Space3D {
    enum class Direction3D { Right, Down, Left, Up, Forward, Back }

    data class Point3D(
        val x: Int, val y: Int, val z: Int,
    ) {
        fun move(direction3D: Direction3D, by: Int = 1): Point3D =
            when (direction3D) {
                Direction3D.Up -> copy(y = y + by)
                Direction3D.Right -> copy(x = x + by)
                Direction3D.Down -> copy(y = y - by)
                Direction3D.Left -> copy(x = x - by)
                Direction3D.Forward -> copy(z = z + by)
                Direction3D.Back -> copy(z = z - by)
            }

        fun neighbors(): Set<Point3D> = Direction3D.entries.map { this.move(it) }.toSet()
    }

    object Parser {
        fun String.toPoint3D(): Point3D =
            this.split(",")
                .map { it.filter { it.isDigit() || it == '-' }.toInt() }
                .let { Point3D(it[0], it[1], it[2]) }

        fun Regex.valuesFor(input: String): List<String> = find(input)?.groupValues?.drop(1)!!
    }
}

object Space2D {
    object Parser {
        fun String.toPoint(): Point =
            this.split(",")
                .map { it.filter { it.isDigit() || it == '-' }.toInt() }
                .let { Point(it[0], it[1]) }

        fun List<String>.toPointToChar(): List<Pair<Point, Char>> {
            val max = this.size
            return mapIndexed { y, s ->
                s.mapIndexed { x, c ->
                    Pair(Point(x, max - y), c)
                }
            }.flatten()
        }

        fun List<String>.toPointChar(): List<PointChar> {
            val max = this.size
            return mapIndexed { y, s ->
                s.mapIndexed { x, c ->
                    PointChar(Point(x, max - y), c)
                }
            }.flatten()
        }
    }

    data class PointChar(val point: Point, val char: Char)

    fun List<PointChar>.getFor(point: Point): PointChar? = this.singleOrNull { it.point == point }

    enum class Direction {
        Right, Down, Left, Up;
    }

    fun Direction.opposite(): Direction =
        when (this) {
            Right -> Left
            Down -> Up
            Left -> Right
            Up -> Down
        }

    data class Point(
        val x: Int, val y: Int
    ) {
        fun move(direction: Direction, by: Int = 1): Point =
            when (direction) {
                Up -> copy(y = y + by)
                Right -> copy(x = x + by)
                Down -> copy(y = y - by)
                Left -> copy(x = x - by)
            }

        fun adjacent(): Set<Point> = Direction.entries.map { this.move(it) }.toSet()

        fun adjacentWithDiagonal(): Set<Point> =
            Direction.entries.flatMap { direction ->
                this.move(direction).let { moved ->
                    listOf(moved) + listOf(moved.move(direction.next()))
                }
            }.toSet()

        fun distanceTo(other: Point): Int =
            listOf(x - other.x, y - other.y).map { it.absoluteValue }.sum()

        fun directionTo(other: Point): Direction {
            val xDelta = (other.x - x).sign
            val yDelta = (other.y - y).sign

            return when {
                yDelta > 0 -> Up
                xDelta > 0 -> Right
                yDelta < 0 -> Down
                xDelta < 0 -> Left
                else -> error("no direction")
            }
        }

        fun lineTo(other: Point): List<Point> {
            val steps = this.distanceTo(other)
            val direction = this.directionTo(other)
            return (1..steps).scan(this) { last, _ -> last.move(direction) }
        }

        operator fun plus(other: Point): Point =
            Point(this.x + other.x, this.y + other.y)

        operator fun minus(other: Point): Point =
            Point(this.x - other.x, this.y - other.y)
    }

    context(Collection<Point>)
    private fun Point.draw(highlight: List<Point>? = null): String =
        firstOrNull { it == this }?.let { if (highlight?.let { this in it } == true) "*" else "#" } ?: "."

    enum class Axis { X, Y }

    context(Collection<Point>)
    fun Axis.toRange(): IntRange {
        fun Point.axisValue(axis: Axis): Int =
            when (axis) {
                Axis.X -> this.x
                Axis.Y -> this.y
            }

        fun min(): Int = minByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0
        fun max(): Int = maxByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0

        return min()..max()
    }

    fun Collection<Point>.print(highlight: List<Point>? = null): String =
        Axis.Y.toRange().reversed().map { y ->
            Axis.X.toRange().map { x ->
                Point(x, y).draw(highlight)
            }.joinToString("")
        }.joinToString(System.lineSeparator())

    interface HasPoints {
        val points: Collection<Point>
        fun move(direction: Direction, by: Int = 1): HasPoints

        fun Collection<Point>.move(direction: Direction, by: Int = 1): Collection<Point> =
            map { it.move(direction, by) }
    }

    fun Collection<Point>.height(): Int = maxOf { it.y }

    fun Collection<Point>.toMaxPoints(): Collection<Point> {
        val minX = this.minOfOrNull { it.x } ?: return this
        val maxX = this.maxOfOrNull { it.x } ?: return this
        val minY = this.minOfOrNull { it.y } ?: return this
        val maxY = this.maxOfOrNull { it.y } ?: return this
        return (minX..maxX).flatMap { x ->
            (minY..maxY).map { y ->
                Point(x, y)
            }
        }
    }
}

object Collections {
    inline fun <reified T> List<List<T>>.transpose(): List<List<T>> {
        val cols = this[0].size
        val rows = size
        return Array(cols) { j ->
            Array(rows) { i ->
                this[i][j]
            }.toList()
        }.toList()
    }

    fun List<String>.splitBy(delimiter: String): List<List<String>> =
        listOf(subList(0, this.indexOf(delimiter)), subList(this.indexOf(delimiter) + 1, this.count()))

    fun List<String>.partitionedBy(delimiter: String): List<List<String>> {
        val indexes = this.indexesOf(delimiter)
        return listOf(this.subList(0, indexes.first())) + partitionAt(indexes) + listOf(
            this.subList(
                indexes.last() + 1,
                this.lastIndex + 1
            )
        )
    }

    private fun <T> List<T>.indexesOf(delimiter: T) =
        mapIndexedNotNull { index, t ->
            index.takeIf { t == delimiter }
        }

    private fun <T> List<T>.partitionAt(indexes: List<Int>) =
        indexes.zipWithNext { a, b ->
            this.subList(a + 1, b)
        }

    fun List<Int>.product(): Int = reduce { acc, i -> acc * i }
    fun List<Long>.product(): Long = reduce { acc, i -> acc * i }
}

object Misc {
    fun String.md5() =
        BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

    fun <T> T.log(): T = also {
        println("")
        if (this is Iterable<*>) this.forEach {
            println(it)
        }
        else println(this)
    }

    fun String.capitalise(): String =
        this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    inline fun <reified T : Enum<T>> T.next(): T {
        val values = enumValues<T>()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }

    fun String.assertEqualTo(other: String) {
        // https://stackoverflow.com/questions/10934743/formatting-output-so-that-intellij-idea-shows-diffs-for-two-texts
        error("expected: $other but was: $this")
    }
}

object Algorithm {

    data class Cost<T>(override val node: Node<T>, override val cost: Int) : HasCost<T>

    interface HasCost<T> : HasNode<T> {
        override val node: Node<T>
        val cost: Int
    }

    interface Node<T> {
        val value: T
        val neighbours: List<HasCost<T>>
    }

    interface HasNode<T> {
        val node: Node<T>
    }

    abstract class Dijkstra<T>(private val start: Node<T>) {
        abstract val isEnd: (HasNode<T>) -> Boolean
        abstract fun neighbours(node: HasNode<T>): List<HasCost<T>>

        private fun shortestPaths(): List<List<HasCost<T>>> {
            val shortestDistances = shortestDistances()
            val startCost = Cost(start, 0) as HasCost<T>
            val shortestPaths = mutableListOf(listOf(startCost))

            fun Map<Node<T>, Int>.getOrMax(node: Node<T>): Int = this.getOrDefault(node, Int.MAX_VALUE)
            fun Map<Node<T>, Int>.matchesNext(last: HasCost<T>, next: HasCost<T>): Boolean =
                this.getOrMax(next.node) == (this.getOrMax(last.node) + next.cost)

            fun visitNext(nodes: List<HasCost<T>>) {
                val last = nodes.last()
                neighbours(last).forEach { next ->
                    if (shortestDistances.matchesNext(last, next) && !isEnd(last)) {
                        shortestPaths.add(nodes + next)
                        visitNext(nodes + next)
                    }
                }
            }

            visitNext(listOf(startCost))

            return shortestPaths.toList().filter { path -> path.any { isEnd(it) } }
        }

        fun shortestPath(): List<Node<T>> =
            shortestPaths()
                .minBy { path -> path.sumOf { it.cost } }
                .map { it.node }

        fun shortestDistances(): Map<Node<T>, Int> {
            val shortestDistances =
                mutableMapOf<Node<T>, Int>()
                    .withDefault { Int.MAX_VALUE }
                    .apply { put(start, 0) }

            fun visitNext(nodes: List<HasCost<T>>) {
                val last = nodes.last()
                neighbours(last).forEach { next ->
                    val nextDistance = shortestDistances[last.node]!! + next.cost
                    if (nextDistance < shortestDistances.getValue(next.node) && !isEnd(last)) {
                        shortestDistances[next.node] = nextDistance
                        visitNext(nodes + next)
                    }
                }
            }
            visitNext(listOf(Cost(start, 0)))

            return shortestDistances
        }
    }

}

object Maps {
    infix fun <T> Map<T, Int>.plusInt(other: Map<T, Int>): Map<T, Int> =
        this.operate(other) { first, second -> first + second }

    infix fun <T> Map<T, Int>.minusInt(other: Map<T, Int>): Map<T, Int> =
        this.operate(other) { first, second -> first - second }

    private fun <T> Map<T, Int>.operate(other: Map<T, Int>, fn: (Int, Int) -> Int): Map<T, Int> =
        this.map { it.key to fn(it.value, other.getOrDefault(it.key, 0)) }.toMap()
}

object Monitoring {
    interface Monitor<T> : (T) -> Unit {
        fun print(): List<String>
    }

    open class PointMonitor(
        private val frame: Set<Point> = setOf()
    ) : Monitor<Set<Point>> {
        private val pointList: MutableList<Set<Point>> = mutableListOf()

        override fun invoke(points: Set<Point>) {
            this.pointList.add(points)
        }

        override fun print(): List<String> =
            pointList.map { points ->
                (points.map { it } + frame).print()
            }
    }

    interface Printable<T> : List<Set<T>> {
        fun print(): List<String>
    }

    abstract class Monitorable<T> : (Printable<T>) -> Unit {
        private val list: MutableList<Printable<T>> = mutableListOf()

        override fun invoke(points: Printable<T>) {
            this.list.add(points)
        }

        fun print(): List<String> = list.map { it.print() }.flatten()
    }
}

