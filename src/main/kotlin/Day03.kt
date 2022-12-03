object Day03 : Day {
    override fun List<String>.part1(): Int = prioritiesCompartments()

    override fun List<String>.part2(): Int = prioritiesBadges()
}

fun List<String>.prioritiesCompartments(): Int =
    map { it.chunked(it.count() / 2) }
        .map { it.commonItem() }
        .sumOf { it.toPriority() }

fun List<String>.prioritiesBadges(): Int =
    chunked(3)
        .map { it.commonItem() }
        .sumOf { it.toPriority() }

fun List<String>.commonItem(): Char =
    fold(get(0).toSet()) { acc, s ->
        s.toSet().intersect(acc.toSet())
    }.first()

fun Char.toPriority(): Int = code - (if (isUpperCase()) (64 - 26) else 96)
