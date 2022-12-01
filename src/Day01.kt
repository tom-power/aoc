object Day01 : Day {
    override fun List<String>.part1(): Int = partitioned().max()

    override fun List<String>.part2(): Int = partitioned().sumTop3()

    @JvmStatic
    fun main(args: Array<String>) {
        super.runMeWith(partOneTestResult = 24000)
    }
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