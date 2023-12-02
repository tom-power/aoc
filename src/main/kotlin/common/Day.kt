package common

interface Day {
    val year: String

    private fun name(): String? = this::class.simpleName

    fun readInputExample(): List<String> =
        readInputFor("example")

    fun readInputFor(extension: String): List<String> =
        Input.readInputWithDirectory("${name()}_${extension}", "aoc$year")

    fun readInput(): List<String> =
        Input.readInputWithDirectory("${name()}", "aoc$year")
}

abstract class DayYear : Day {
    override val year: String = "Year"
}

abstract class Day22 : Day {
    override val year: String = "22"
}

abstract class Day23 : Day {
    override val year: String = "23"
}

