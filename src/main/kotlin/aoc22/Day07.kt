package aoc22

object Day07 : Day<String, Int> {
    override fun List<String>.part1(): Int = with(FileSys()) { dirSizes().sumOfSmall() }

    override fun List<String>.part2(): Int = with(FileSys()) { dirSizes().findSmallestToDelete() }
}

private fun Map<String, Int>.sumOfSmall(): Int =
    values
        .filter { it < 100_000 }
        .sum()

private val totalSpace = 70_000_000
private val neededSpace = 30_000_000

private fun Map<String, Int>.findSmallestToDelete(): Int {
    val usedSpace = values.max()
    return values
        .filter { (neededSpace + usedSpace - it) < totalSpace }
        .min()
}

class FileSys {
    private var pwd = "/"
    private val parents = mutableListOf<String>()
    private val dirSizeMap = mutableMapOf("/" to 0)

    fun List<String>.dirSizes(): MutableMap<String, Int> =
        updateDirSizeMap()
            .let { dirSizeMap }

    private fun List<String>.updateDirSizeMap() {
        forEach { row ->
            when {
                row.startsWith("\$ cd ") -> row.replace("\$ cd ", "").updateLocation()
                row.first().isDigit()    -> (row.filter { it.isDigit() }.toIntOrNull() ?: 0).updateDirSizeMap()
            }
        }
    }

    private fun getDirKey(indexTo: Int, pwd: String): String = parents.subList(0, indexTo).joinToString("/") + pwd

    private fun Int.updateDirSizeMap() {
        dirSizeMap[getDirKey(parents.size, pwd)] = this + (dirSizeMap[getDirKey(parents.size, pwd)] ?: 0)
        parents.forEachIndexed { index, parent ->
            dirSizeMap[getDirKey(index, parent)] = this + (dirSizeMap[getDirKey(index, parent)] ?: 0)
        }
    }

    private fun String.updateLocation() {
        when (this) {
            ".." -> parents.removeLast()
            else -> (this).also { parents.add(pwd) }
        }.let { pwd = it }
    }
}
