package aoc22

object Day00 : Day<String, Int> {
    override fun List<String>.part1(): Int = part1Day00()

    override fun List<String>.part2(): Int = part2Day00()

    private fun List<String>.part1Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }

    private fun List<String>.part2Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}
