package common

import java.io.File

fun Year.readInputExample(): List<String> = readInput("_example")

fun Year.readInput(suffix: String? = null): List<String> {
    val directory = "aoc$year"
    val path = "$name${suffix.orEmpty()}.txt"
    return File("src/test/resources", "$directory/$path").readLines()
}
