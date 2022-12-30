package aoc22

import aoc22.Matrix.Direction.*
import aoc22.Matrix.Point
import aoc22.Misc.next
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue
import kotlin.math.sign

object Input {
    fun readInputWithFallback(name: String, fallback: String): List<String> =
        readInput(name) ?: readInput("$fallback/$name")!!

    private fun readInput(name: String) = File("src/main/resources", "$name.txt").takeIf { it.isFile }?.readLines()
}

object Matrix {
    enum class Direction { Right, Down, Left, Up }

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

        fun getAdjacent(): Set<Point> = values().map { this.move(it) }.toSet()

        fun getAdjacentWithDiagonal(): Set<Point> =
            values().flatMap { direction ->
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
    }

    context(Collection<Point>)
    private fun Point.draw(): String = firstOrNull { it == this }?.let { "#" } ?: "."

    enum class Axis { X, Y }

    context(Collection<Point>)
    private fun Axis.toRange(): IntRange {
        fun Point.axisValue(axis: Axis): Int =
            when (axis) {
                Axis.X -> this.x
                Axis.Y -> this.y
            }

        fun min(): Int = minByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0
        fun max(): Int = maxByOrNull { it.axisValue(this) }?.axisValue(this) ?: 0

        return min()..max()
    }

    fun Collection<Point>.print(): String =
        Axis.Y.toRange().reversed().map { y ->
            Axis.X.toRange().map { x ->
                Point(x, y).draw()
            }.joinToString("")
        }.joinToString(System.lineSeparator())

    interface HasPoints {
        val points: Collection<Point>
        fun move(direction: Direction, by: Int = 1): HasPoints

        fun Collection<Point>.move(direction: Direction, by: Int = 1): Collection<Point> = map { it.move(direction, by) }
    }

    fun Collection<Point>.height(): Int = maxOf { it.y }
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
        if (this is Iterable<*>) this.forEach {
            println(it)
        }
        else println(this)
    }

    inline fun <reified T : Enum<T>> T.next(): T {
        val values = enumValues<T>()
        val nextOrdinal = (ordinal + 1) % values.size
        return values[nextOrdinal]
    }
}

object Parser {
    fun String.toPoint(): Point =
        this.split(",")
            .map { it.filter { it.isDigit() || it == '-' }.toInt() }
            .let { Point(it[0], it[1]) }

    fun List<String>.parsePointChars(): List<Pair<Point, Char>> =
        mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Pair(Point(x, y), c)
            }
        }.flatten()
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