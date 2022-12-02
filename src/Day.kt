interface Day {
    fun List<String>.part1(): Int

    fun List<String>.part2(): Int

    fun runMeWith(
        partOneTestExpected: Int,
        partOneExpected: Int?,
        partTwoExpected: Int?,
    ) {
        readInput("${this::class.simpleName}_test").run {
            part1().checkEquals(partOneTestExpected)
        }

        readInput("${this::class.simpleName}").run {
            part1().print().checkEquals(partOneExpected)
            part2().print().checkEquals(partTwoExpected)
        }
    }

    fun Int.checkEquals(expected: Int?) {
        expected?.let { check(this == it) }
    }

    fun Int.print(): Int = also { println(this) }
}