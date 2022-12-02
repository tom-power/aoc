object Day01 : Day {
    override fun List<String>.part1(): Int = partitionedBy("").summed().max()

    override fun List<String>.part2(): Int = partitionedBy("").summed().sumOfTop3()

    @JvmStatic
    fun main(args: Array<String>) {
        super.runMeWith(
            partOneTestExpected = 24000,
            partOneExpected = 70613,
            partTwoExpected = 205805
        )
    }
}

fun List<String>.partitionedBy(delimiter: String): List<List<String>> =
    partitionAt(this.indexesOf(delimiter))

fun List<List<String>>.summed(): List<Int> =
    this.map { it.map(String::toInt) }
        .map(List<Int>::sum)

fun List<Int>.sumOfTop3(): Int =
    sortedByDescending { it }
        .take(3)
        .sum()

fun <T> List<T>.indexesOf(delimiter: T) = mapIndexedNotNull { index, t -> index.takeIf { t == delimiter } }
fun <T> List<T>.partitionAt(indexes: List<Int>) = indexes.zipWithNext { a, b -> this.subList(a + 1, b) }