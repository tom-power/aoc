package aoc23

import common.Year23
import aoc23.Day09Domain.Oasis
import aoc23.Day09Parser.toOasis
import aoc23.Day09Solution.part1Day09
import aoc23.Day09Solution.part2Day09

object Day09 : Year23 {
    fun List<String>.part1(): Int = part1Day09()

    fun List<String>.part2(): Int = part2Day09()
}

object Day09Solution {
    fun List<String>.part1Day09(): Int =
        with(toOasis()) {
            reportWithDifferences { predictNext() }
        }.sum()

    fun List<String>.part2Day09(): Int =
        with(toOasis()) {
            reportWithDifferences { predictPrevious() }
        }.sum()
}

object Day09Domain {
    data class Oasis(
        val report: List<List<Int>>
    ) {
        fun reportWithDifferences(predict: List<List<Int>>.() -> Int): List<Int> =
            report
                .map { history ->
                    history.withDifferences().predict()
                }

        fun List<List<Int>>.predictNext(): Int = this.map { it.last() }.sum()

        fun List<List<Int>>.predictPrevious(): Int =
            this.map { it.first() }
                .foldRight(0) { i, acc -> i - acc }

        private fun List<Int>.withDifferences(): List<List<Int>> =
            generateSequence(this) { ints ->
                ints.windowed(2) { (x, y) -> y - x }
            }
                .takeWhile { it.any { difference -> difference != 0 } }
                .toList()
    }
}

object Day09Parser {
    fun List<String>.toOasis(): Oasis =
        Oasis(
            report = this.map { history -> history.split(" ").map { it.toInt() } }
        )
}
