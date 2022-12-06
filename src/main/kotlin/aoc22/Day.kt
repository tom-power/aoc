package aoc22

import aoc22.Input.readInputWithFallback

interface Day<T, R> {
    fun List<T>.part1(): R

    fun List<T>.part2(): R

    private fun name(): String? = this::class.simpleName

    fun readInputExample(): List<String> =
        readInputWithFallback("${name()}_example", "aoc22")

    fun readInput(): List<String> =
        readInputWithFallback("${name()}", "aoc22")
}