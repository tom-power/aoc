interface Day {
    fun List<String>.part1(): Int

    fun List<String>.part2(): Int

    fun runMeWith(partOneTestResult: Int) {
        readInput("${this::class.simpleName}_test").run {
            check(part1() == partOneTestResult)
        }

        readInput("${this::class.simpleName}").run {
            println(part1())
            println(part2())
        }
    }
}