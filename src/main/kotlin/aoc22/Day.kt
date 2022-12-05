package aoc22

interface Day<T, R> {
    fun List<T>.part1(): R

    fun List<T>.part2(): R

    fun readDaysInputTest(): List<String> = readInput("${this::class.simpleName}_test")
    fun readDaysInput(): List<String> = readInput("${this::class.simpleName}")

}