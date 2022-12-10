package aoc22

object Day04 : Day<String, Int, Int> {
    override fun List<String>.part1(): Int = countOfAssignmentPairsWhere(::isOverlapAll)

    override fun List<String>.part2(): Int = countOfAssignmentPairsWhere(::isOverlapAny)

    private fun List<String>.countOfAssignmentPairsWhere(predicate: (Pair<IntRange, IntRange>) -> Boolean): Int =
        map { it.toAssignmentPairs() }
            .filter { predicate(it) }
            .count()

    private fun String.toAssignmentPairs(): Pair<IntRange, IntRange> =
        split(",")
            .run { this[0].toRange() to this[1].toRange() }

    private fun String.toRange(): IntRange =
        split("-")
            .run { this[0].toInt()..this[1].toInt() }

    private fun isOverlapAll(ranges: Pair<IntRange, IntRange>) =
        ranges.first.intersect(ranges.second).let { it == ranges.first.toSet() || it == ranges.second.toSet() }

    private fun isOverlapAny(ranges: Pair<IntRange, IntRange>) =
        ranges.first.intersect(ranges.second).isNotEmpty()

    private fun IntRange.intersect(other: IntRange) =
        this.toSet().intersect(other.toSet())
}