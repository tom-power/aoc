package aoc22

import aoc22.Input.readInputWithFallback

interface Day {
    private fun name(): String? = this::class.simpleName

    fun readInputExample(): List<String> =
        readInputWithFallback("${name()}_example", "aoc22")

    fun readInputExample2(): List<String> =
        readInputWithFallback("${name()}_example2", "aoc22")

    fun readInput(): List<String> =
        readInputWithFallback("${name()}", "aoc22")
}