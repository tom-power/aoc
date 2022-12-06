package aoc22

object Day06 : Day<String, Int> {
    override fun List<String>.part1(): Int = detectDistinct(4)

    override fun List<String>.part2(): Int = detectDistinct(14)

    private fun List<String>.detectDistinct(size: Int): Int =
        this[0].windowed(size, 1)
            .mapIndexedNotNull { index, s ->
                (index + size).takeIf { s.toSet().size == size }
            }
            .first()
}
