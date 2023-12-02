package aoc22

import common.Year22

object Day06: Year22() {
    fun List<String>.part1(): Int = detectDistinct(4)

    fun List<String>.part2(): Int = detectDistinct(14)

    private fun List<String>.detectDistinct(size: Int): Int =
        this[0].windowed(size, 1)
            .mapIndexedNotNull { index, s ->
                (index + size).takeIf { s.toSet().size == size }
            }
            .first()
}
