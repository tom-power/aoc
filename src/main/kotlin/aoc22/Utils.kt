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

    enum class Direction { R, D, L, U }

    data class Point(
        val x: Int, val y: Int
    ) {

        fun add(x: Int, y: Int) = Point(x = this.x + x, y = this.y + y)

        fun move(direction: Direction): Point =
            when (direction) {
                R -> copy(x = x + 1)
                D -> copy(y = y + 1)
                L -> copy(x = x - 1)
                U -> copy(y = y - 1)
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
                xDelta > 0 -> R
                yDelta > 0 -> D
                xDelta < 0 -> L
                yDelta < 0 -> U
                else -> error("no direction")
            }
        }

        fun lineTo(other: Point): List<Point> {
            val steps = this.distanceTo(other)
            val direction = this.directionTo(other)
            return (1..steps).scan(this) { last, _ -> last.move(direction) }
        }
    }

    fun Collection<Point>.print(): String {
        val colMin = minByOrNull { it.x }?.x ?: 0
        val colMax = maxByOrNull { it.x }?.x ?: 0
        val rowMin = minByOrNull { it.y }?.y ?: 0
        val rowMax = maxByOrNull { it.y }?.y ?: 0
        return (rowMin..rowMax).map { y ->
            val joinToString = (colMin..colMax)
                .map { x ->
                    this.firstOrNull { it == Point(x, y) }?.let { "#" } ?: "."
                }
                .joinToString("")
            joinToString
        }.joinToString(System.lineSeparator())
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