package common

import java.io.File

fun Year.readInputExample(): List<String> = readInput("_example")

fun Year.readInput(extension: String? = null): List<String> {
    val directory = "aoc$year"
    val path = "${name()}${extension.orEmpty()}.txt"
    return File("src/test/resources", "$directory/$path").readLines()
}
