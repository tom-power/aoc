package aoc23

import aoc23.Day13Domain.PointOfIncidence
import aoc23.Day13Parser.toPointsOfIncidence
import common.Collections.partitionedBy
import common.Strings.transpose
import common.Year23

object Day13 : Year23 {
    fun List<String>.part1(): Int =
        toPointsOfIncidence(targetNumberOfDifferences = 0)
            .summaryOfNotes()

    fun List<String>.part2(): Int =
        toPointsOfIncidence(targetNumberOfDifferences = 1)
            .summaryOfNotes()
}

object Day13Domain {
    data class PointOfIncidence(
        private val notes: List<List<String>>,
        private val targetNumberOfDifferences: Int
    ) {
        fun summaryOfNotes(): Int =
            notes.sumOf { note ->
                note.linesAboveReflection()?.let { it * 100 }
                    ?: note.transpose().linesAboveReflection()!!
            }

        private fun List<String>.linesAboveReflection(): Int? = this.let(::Note).linesAboveLineOfReflection()

        private inner class Note(
            private val lines: List<String>
        ) {
            fun linesAboveLineOfReflection(): Int? =
                lines
                    .mapIndexedNotNull { index, _ ->
                        index.takeIf {
                            index > 0
                                && lines.differencesToEdgeFrom(index - 1 to index) == targetNumberOfDifferences
                        }
                    }
                    .firstOrNull()

            private fun List<String>.differencesToEdgeFrom(indexes: Pair<Int, Int>): Int {
                val firstLine = this.getOrNull(indexes.first)
                val secondLine = this.getOrNull(indexes.second)

                return when {
                    firstLine == null || secondLine == null -> 0
                    else ->
                        firstLine.numberOfDifferences(secondLine) +
                            differencesToEdgeFrom(indexes.first - 1 to indexes.second + 1)
                }
            }

            private fun String.numberOfDifferences(other: String): Int =
                this.zip(other).count { (a, b) -> a != b }
        }
    }
}

object Day13Parser {
    fun List<String>.toPointsOfIncidence(targetNumberOfDifferences: Int): PointOfIncidence =
        PointOfIncidence(
            notes = this.partitionedBy(""),
            targetNumberOfDifferences = targetNumberOfDifferences
        )
}
