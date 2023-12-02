package common

import aoc22.Input

interface Day {
    private fun name(): String? = this::class.simpleName

    fun readInputExample(): List<String> =
        readInputFor("example")

    fun readInputFor(extension: String): List<String> =
        Input.readInputWithFallback("${name()}_${extension}", "aoc22")

    fun readInput(): List<String> =
        Input.readInputWithFallback("${name()}", "aoc22")
}