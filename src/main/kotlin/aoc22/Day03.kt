package aoc22

object Day03 : Day {
    override fun List<String>.part1(): Int = toCompartments().toSumOfCommon()

    override fun List<String>.part2(): Int = toTrios().toSumOfCommon()
}

fun List<String>.toCompartments(): List<List<String>> =
    map { rucksackItems ->
        rucksackItems.chunked(rucksackItems.count() / 2)
    }

fun List<String>.toTrios(): List<List<String>> =
    chunked(3)

fun List<List<String>>.toSumOfCommon(): Int =
    map { it.commonItem() }
        .sumOf { it.toPriority() }

fun List<String>.commonItem(): Char =
    fold(get(0).toSet()) { acc, s ->
        s.toSet().intersect(acc.toSet())
    }.first()

fun Char.toPriority(): Int = code - asciiIndex() + upperCasePriority()

fun Char.asciiIndex(): Int = if (isUpperCase()) 64 else 96
fun Char.upperCasePriority(): Int = if (isUpperCase()) 26 else 0
