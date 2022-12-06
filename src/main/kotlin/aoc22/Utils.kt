package aoc22

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/main/resources", "$name.txt")
    .readLines()

/**
 * Converts string to aoc22.md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

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

fun List<String>.partitionedBy(delimiter: String): List<List<String>> =
    partitionAt(this.indexesOf(delimiter))

private fun <T> List<T>.indexesOf(delimiter: T) = mapIndexedNotNull { index, t -> index.takeIf { t == delimiter } }

private fun <T> List<T>.partitionAt(indexes: List<Int>) = indexes.zipWithNext { a, b -> this.subList(a + 1, b) }

fun <T> T.log(): T = also { println(it) }