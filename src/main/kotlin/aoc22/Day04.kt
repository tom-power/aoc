package aoc22

object Day04 : Day {
    override fun List<String>.part1(): Int = day04 { it.first.containsAll(it.second) }

    override fun List<String>.part2(): Int = day04 { it.first.containsAny(it.second) }
}

fun List<String>.day04(discriminator: (Pair<IntRange, IntRange>) -> Boolean ): Int =
    map { it.toRanges() }
        .filter { discriminator(it) }
        .count()

fun String.toRanges(): Pair<IntRange, IntRange> =
    split(",")
        .run { this[0].toRange() to this[1].toRange() }

fun String.toRange(): IntRange =
    split("-")
        .run { this[0].toInt()..this[1].toInt() }

fun IntRange.containsAll(other: IntRange) =
    this.intersect(other).let { it == this.toSet() || it == other.toSet() }

fun IntRange.containsAny(other: IntRange) =
    this.intersect(other).isNotEmpty()

fun IntRange.intersect(other: IntRange) =
    this.toSet().intersect(other.toSet())