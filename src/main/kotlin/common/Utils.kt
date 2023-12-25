package common

import common.Collections.transpose
import common.Misc.next
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Space2D.toLoggable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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

        fun List<String>.toPointToChars(): List<Pair<Point, Char>> {
            val max = this.size
            return mapIndexed { y, s ->
                s.mapIndexed { x, c ->
                    Pair(Point(x, max - y), c)
                }
            }.flatten()
        }

        fun List<String>.toPointChars(): List<PointChar> {
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
        East, South, West, North;
    }

    fun Direction.opposite(): Direction =
        when (this) {
            East -> West
            South -> North
            West -> East
            North -> South
        }

    val clockWiseDirections = listOf(East, South, West, North)

    fun Direction.turn(side: Side): Direction =
        clockWiseDirections.let {
            val i = it.indexOf(this) + side.toInt()
            it[(i % it.size + it.size) % it.size]
        }

    private fun Side.toInt(): Int =
        when (this) {
            Side.Right -> +1
            Side.Left -> -1
        }

    enum class Side { Right, Left }

    data class Point(
        val x: Int, val y: Int
    ) : Comparable<Point> {
        fun move(direction: Direction, by: Int = 1): Point =
            when (direction) {
                North -> copy(y = y + by)
                East -> copy(x = x + by)
                South -> copy(y = y - by)
                West -> copy(x = x - by)
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
                yDelta > 0 -> North
                xDelta > 0 -> East
                yDelta < 0 -> South
                xDelta < 0 -> West
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

        override fun compareTo(other: Point): Int =
            y.compareTo(other.y)
                .takeIf { it != 0 }
                ?: x.compareTo(other.x)
    }

    context(Collection<Point>)
    private fun Point.isEdge(): Boolean = this in toMaxPoints()

    context(Collection<Point>)
    fun Point.nextIn(direction: Direction): Point? =
        when (direction) {
            East -> filter { it.y == this.y && it.x > this.x }.minByOrNull { it.x }
            South -> filter { it.x == this.x && it.y < this.y }.maxByOrNull { it.y }
            West -> filter { it.y == this.y && it.x < this.x }.maxByOrNull { it.x }
            North -> filter { it.x == this.x && it.y > this.y }.minByOrNull { it.y }
        }

    context(Collection<Point>)
    fun Point.lastIn(direction: Direction): Point =
        when (direction) {
            East -> Point(Axis.X.max(), this.y)
            South -> Point(this.x, Axis.Y.min())
            West -> Point(Axis.X.min(), this.y)
            North -> Point(this.x, Axis.Y.max())
        }

    val Collection<Point>.topLeft: Point
        get() =
            Point(
                x = Axis.X.min(),
                y = Axis.Y.max()
            )

    val Collection<Point>.bottomRight: Point
        get() =
            Point(
                x = Axis.X.max(),
                y = Axis.Y.min()
            )

    enum class Axis { X, Y }

    context(Collection<Point>)
    fun Axis.toRange(): IntRange {
        return min()..max()
    }

    context(Collection<Point>)
    fun Axis.min(): Int = minByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0

    context(Collection<Point>)
    fun Axis.max(): Int = maxByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0

    private
    fun Point.axisValue(axis: Axis): Int =
        when (axis) {
            Axis.X -> this.x
            Axis.Y -> this.y
        }

    fun Collection<Point>.toLoggable(
        highlight: Set<Point>? = null,
        highlightWith: String = "*",
        edges: Set<Point> = emptySet(),
        showEdge: Boolean = false
    ): String {
        return Axis.Y.toRange().reversed().joinToString(System.lineSeparator()) { y ->
            Axis.X.toRange().joinToString("") { x ->
                val point = Point(x, y)
                when {
                    highlight?.contains(point) == true -> highlightWith
                    point in this -> "#"
                    point in edges ->
                        when {
                            showEdge -> "#"
                            else -> "."
                        }

                    else -> "."
                }
            }
        }
    }

    fun Map<Point, Char>.toLoggable(
        highlight: Set<Point>? = null,
        highlightWith: String = "*",
    ): String {
        return with(this.keys) {
            Axis.Y.toRange().reversed().joinToString(System.lineSeparator()) { y ->
                Axis.X.toRange().joinToString("") { x ->
                    val point = Point(x, y)
                    when {
                        highlight?.contains(point) == true -> highlightWith
                        else -> this@toLoggable[point].toString()
                    }
                }
            }
        }
    }

    interface HasPoints {
        val points: Collection<Point>
        fun move(direction: Direction, by: Int = 1): HasPoints

        fun Collection<Point>.move(direction: Direction, by: Int = 1): Collection<Point> =
            map { it.move(direction, by) }
    }

    fun Collection<Point>.height(): Int = maxOf { it.y }

    fun Collection<Point>.toEdges(): Collection<Point> {
        val minX = this.minOfOrNull { it.x }
        val maxX = this.maxOfOrNull { it.x }
        val minY = this.minOfOrNull { it.y }
        val maxY = this.maxOfOrNull { it.y }
        return toMaxPoints().filter { it.x == minX || it.x == maxX || it.y == minY || it.y == maxY }
    }

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

    fun List<String>.partitionedBy(delimiter: String): List<List<String>> {
        val indexes =
            this.indexesOf(delimiter)
                .takeIf { it.isNotEmpty() }
                ?: return listOf(this)
        val first = this.subList(0, indexes.first())
        val last = this.subList(indexes.last() + 1, this.lastIndex + 1)
        return listOf(first) + partitionAt(indexes) + listOf(last)
    }

    private fun <T> List<T>.indexesOf(delimiter: T) =
        mapIndexedNotNull { index, t ->
            index.takeIf { t == delimiter }
        }

    private fun <T> List<T>.partitionAt(indexes: List<Int>): List<List<T>> =
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
        fun toLoggable(): List<String>
    }

    class PointCharMonitor(
        private val canvas: Map<Point, Char> = mapOf(),
        private val edges: Set<Point> = setOf(),
        private val highlightWith: String = "*",
    ) : Monitor<Map<Point, Char>> {
        private val frames: MutableList<Set<Point>> = mutableListOf()
        override fun toLoggable(): List<String> =
            frames.map { frame ->
                canvas.toLoggable(
                    highlight = frame,
                    highlightWith = highlightWith,
                )
            }

        override fun invoke(p1: Map<Point, Char>) {
            TODO("Not yet implemented")
        }

        operator fun invoke(frame: Set<Point>) {
            frames.add(frame)
        }
    }

    open class PointMonitor(
        private val canvas: Set<Point> = setOf(),
        private val edges: Set<Point> = setOf(),
        private val highlightWith: String = "*",
        private val showEdge: Boolean = false,
    ) : Monitor<Set<Point>> {
        private val frames: MutableList<Set<Point>> = mutableListOf()

        override fun invoke(frame: Set<Point>) {
            this.frames += frame
        }

        override fun toLoggable(): List<String> =
            frames.map { frame ->
                (this.canvas).toLoggable(
                    highlight = frame,
                    highlightWith = highlightWith,
                    edges = edges,
                    showEdge = showEdge,
                )
            }
    }
}

object Strings {
    fun String.replaceFirst(toReplace: Char, replacement: Char): String =
        replaceAt(this.indexOfFirst { it == toReplace }, replacement)

    fun String.replaceAt(index: Int, replacement: Char) =
        this.substring(0, index) + replacement + this.substring(index + 1)

    fun List<String>.transpose(): List<String> =
        map { it.toList() }
            .transpose()
            .map { it.joinToString() }
}

object Parallel {
    fun <T> Iterable<T>.forEachParallel(fn: suspend (T) -> Unit): Unit =
        runBlocking {
            forEach { async(Dispatchers.Default) { fn(it) } }
        }
}