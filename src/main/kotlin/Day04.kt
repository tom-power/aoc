object Day04 : Day {
    override fun List<String>.part1(): Int = part1Day04()

    override fun List<String>.part2(): Int = part2Day04()
}

fun List<String>.part1Day04(): Int =
    map { it.toRanges() }
        .filter { it.first.containsAll(it.second) }
        .count()

fun List<String>.part2Day04(): Int =
    map { it.toRanges() }
        .filter { it.first.containsAny(it.second) }
        .count()

fun String.toRanges(): Pair<IntRange, IntRange> = split(",").run { this[0].toRange() to this[1].toRange() }

fun String.toRange(): IntRange = this.split("-").run { this[0].toInt()..this[1].toInt() }

fun IntRange.containsAll(other: IntRange) =
    this.toList().containsAll(other.toList()) || other.toList().containsAll(this.toList())

fun IntRange.containsAny(other: IntRange) =
    this.toSet().intersect(other.toSet()).isNotEmpty()