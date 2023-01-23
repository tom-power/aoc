package aoc22

import aoc22.Input.readInputWithFallback

interface Day {
    private fun name(): String? = this::class.simpleName

    fun readInputExample(): List<String> =
        readInputFor("example")

    fun readInputFor(extension: String): List<String> =
        readInputWithFallback("${name()}_${extension}", "aoc22")

    fun readInput(): List<String> =
        readInputWithFallback("${name()}", "aoc22")
}