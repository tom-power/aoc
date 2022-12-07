package aoc22

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

object Input {
    fun readInputWithFallback(name: String, fallback: String): List<String> =
        readInput(name) ?: readInput("$fallback/$name")!!

    private fun readInput(name: String) =
        File("src/main/resources", "$name.txt")
            .takeIf { it.isFile }
            ?.readLines()
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
        return partitionAt(indexes) + listOf(this.subList(indexes.last() + 1, this.lastIndex + 1) )
    }

    private fun <T> List<T>.indexesOf(delimiter: T) =
        mapIndexedNotNull { index, t -> index.takeIf { t == delimiter } }

    private fun <T> List<T>.partitionAt(indexes: List<Int>) =
        indexes
            .zipWithNext { a, b ->
                this.subList(a + 1, b)
            }
}

object Misc {
    fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

    fun <T> T.log(): T = also { println(it) }
}
