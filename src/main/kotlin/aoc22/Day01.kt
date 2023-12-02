package aoc22

import aoc22.Collections.partitionedBy
import common.Day

object Day01 : Day {
    fun List<String>.part1(): Int = partitionedBy("").summed().max()

    fun List<String>.part2(): Int = partitionedBy("").summed().sumOfTop3()

    private fun List<List<String>>.summed(): List<Int> =
        this.map { it.map(String::toInt) }
            .map(List<Int>::sum)

    private fun List<Int>.sumOfTop3(): Int =
        sortedByDescending { it }
            .take(3)
            .sum()

}