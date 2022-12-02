interface Day {
    fun List<String>.part1(): Int

    fun List<String>.part2(): Int

    fun readDaysInputTest(): List<String> = readInput("${this::class.simpleName}_test")
    fun readDaysInput(): List<String> = readInput("${this::class.simpleName}")

}