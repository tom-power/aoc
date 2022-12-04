object Day00 : Day {
    override fun List<String>.part1(): Int = part1Day00()

    override fun List<String>.part2(): Int = part2Day00()
}

fun List<String>.part1Day00(): Int =
    map { it.split(",") }.also { println(it) }
        .let { 0 }

fun List<String>.part2Day00(): Int =
    map { it.split(",") }.also { println(it) }
        .let { 0 }
