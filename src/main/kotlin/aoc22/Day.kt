package aoc22

import aoc22.Input.readInput

interface Day<T, R> {
    fun List<T>.part1(): R

    fun List<T>.part2(): R

    fun readDaysInputExample(): List<String> =
        readInputWithFallback("${this::class.simpleName}_example", "aoc22")

    fun readDaysInput(): List<String> =
        readInputWithFallback("${this::class.simpleName}", "aoc22")

    private fun readInputWithFallback(name: String, fallback: String): List<String> =
        readInput(name) ?: readInput("$fallback/$name")!!

}