package aoc23

import aoc23.Day13Domain.PointOfIncidence
import aoc23.Day13Parser.toPointsOfIncidence
import aoc23.Day13Solution.part1Day13
import aoc23.Day13Solution.part2Day13
import common.Collections.partitionedBy
import common.Strings.transpose
import common.Year23

object Day13 : Year23 {
    fun List<String>.part1(): Int = part1Day13()

    fun List<String>.part2(): Int = part2Day13()
}

object Day13Solution {
    fun List<String>.part1Day13(): Int =
        toPointsOfIncidence()
            .summary()

    fun List<String>.part2Day13(): Int =
        toPointsOfIncidence()
            .summary()
}

object Day13Domain {
    data class PointOfIncidence(
        private val notes: List<List<String>>
    ) {
        fun summary(): Int =
            notes.sumOf { note ->
                note.linesAbove()?.let { it * 100 }
                    ?: note.transpose().linesAbove()!!
            }

        private fun List<String>.linesAbove(): Int? = this.let(::Note).linesAboveLineOfReflection()
    }

    data class Note(
        private val lines: List<String>
    ) {
        fun linesAboveLineOfReflection(): Int? =
            lines
                .mapIndexedNotNull { index, line ->
                    index.takeIf {
                        lines.getOrNull(index - 1) == line
                            && lines.isSymmetricalAt(Pair(index - 2, index + 1))
                    }
                }
                .firstOrNull()
    }

    private fun List<String>.isSymmetricalAt(indexes: Pair<Int, Int>): Boolean {
        val first = this.getOrNull(indexes.first)
        val second = this.getOrNull(indexes.second)

        if (first == null || second == null)
            return true

        if (first != second)
            return false

        return isSymmetricalAt(Pair(indexes.first - 1, indexes.second + 1))
    }
}

object Day13Parser {
    fun List<String>.toPointsOfIncidence(): PointOfIncidence =
        PointOfIncidence(
            notes = this.partitionedBy("")
        )
}
