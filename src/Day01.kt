fun main() {
    fun part1(input: List<String>): Int = input.partitioned().max()

    fun part2(input: List<String>): Int = input.partitioned().sumTop3()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun List<String>.partitioned(): List<Int> =
    partitionAt(this.indexesOf(""))
        .map { it.map(String::toInt) }
        .map(List<Int>::sum)

fun List<Int>.sumTop3(): Int =
    sortedByDescending { it }
        .take(3)
        .sum()

fun <T> List<T>.indexesOf(delimiter: T) = mapIndexedNotNull { index, t -> index.takeIf { t == delimiter } }
fun <T> List<T>.partitionAt(indexes: List<Int>) = indexes.zipWithNext { a, b -> this.subList(a + 1, b) }