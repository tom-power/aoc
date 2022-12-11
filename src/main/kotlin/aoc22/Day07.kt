package aoc22

import aoc22.Day07Domain.Command
import aoc22.Day07Domain.Down
import aoc22.Day07Domain.Ls
import aoc22.Day07Domain.Cd
import aoc22.Day07Domain.Up
import aoc22.Day07Parser.parseCommands
import aoc22.Day07Solution.part1Day07
import aoc22.Day07Solution.part2Day07

object Day07 : Day<String, Int, Int> {
    override fun List<String>.part1(): Int = part1Day07()

    override fun List<String>.part2(): Int = part2Day07()
}

object Day07Solution {
    fun List<String>.part1Day07(): Int = getDirSizes().sumOfSmall()

    private fun List<Int>.sumOfSmall(): Int =
        filter { it < 100_000 }.sum()

    fun List<String>.part2Day07(): Int = getDirSizes().findSmallestToDelete()

    private val totalSpace = 70_000_000
    private val neededSpace = 30_000_000

    private fun List<Int>.findSmallestToDelete(): Int =
        filter { (neededSpace + max() - it) < totalSpace }.min()

    private fun List<String>.getDirSizes(): List<Int> =
        parseCommands()
            .let { with(Day07FileSys()) { it.dirSizes() } }
}

class Day07FileSys {
    private var pwd = "/"
    private val parents = mutableListOf<String>()
    private val dirSizeMap = mutableMapOf("/" to 0)

    fun List<Command>.dirSizes(): List<Int> =
        updateDirSizeMap()
            .let { dirSizeMap.values.toList() }

    private fun List<Command>.updateDirSizeMap() {
        forEach {
            when (it) {
                is Cd -> it.updateLocation()
                is Ls -> it.updateDirSizeMap()
            }
        }
    }

    private fun Ls.updateDirSizeMap() {
        dirSizeMap[getDirKey(parents.size, pwd)] = this.fileSize + (dirSizeMap[getDirKey(parents.size, pwd)] ?: 0)
        parents.forEachIndexed { index, parent ->
            dirSizeMap[getDirKey(index, parent)] = this.fileSize + (dirSizeMap[getDirKey(index, parent)] ?: 0)
        }
    }

    private fun Cd.updateLocation() {
        when (this) {
            Up      -> parents.removeLast()
            is Down -> this.path.also { parents.add(pwd) }
        }.let { pwd = it }
    }

    private fun getDirKey(indexTo: Int, pwd: String): String = parents.subList(0, indexTo).joinToString("/") + pwd

}

class Day07FileSysFold {
    data class State(
        var pwd: String,
        val parents: List<String>,
        val dirSizeMap: Map<String, Int>,
    )

    fun List<Command>.dirSizes(): List<Int> {
        val initialState =
            State(
                pwd = "/",
                parents = emptyList(),
                dirSizeMap = mutableMapOf("/" to 0)
            )

        return fold(initialState) { acc, command ->
            when (command) {
                is Cd -> acc.cd(command)
                is Ls -> acc.ls(command)
            }
        }.dirSizeMap.values.toList()
    }

    private fun State.cd(cd: Cd): State =
        when (cd) {
            is Down -> this.cdDown(cd)
            Up      -> this.cdUp(cd as Up)
        }

    private fun State.cdDown(down: Down): State =
        copy(
            pwd = down.path,
            parents = parents + listOf(down.path)
        )

    private fun State.cdUp(up: Up): State =
        copy(
            pwd = parents.last(),
            parents = parents.dropLast(1)
        )

    private fun State.ls(ls: Ls): State {
        val dirKey = getDirKey(parents.size, pwd)
        val thisDirSizeMap = mutableMapOf<String, Int>()

        thisDirSizeMap[dirKey] = ls.fileSize + (dirSizeMap[dirKey] ?: 0)
        parents.forEachIndexed { index, parent ->
            val dirKey1 = getDirKey(index, parent)
            thisDirSizeMap[dirKey1] = ls.fileSize + (dirSizeMap[dirKey1] ?: 0)
        }

        return copy(dirSizeMap = dirSizeMap + thisDirSizeMap)
    }

    private fun State.getDirKey(indexTo: Int, pwd: String): String = parents.subList(0, indexTo).joinToString("/") + pwd

}

object Day07Domain {
    sealed class Command
    sealed class Cd : Command()
    object Up : Cd()
    data class Down(val path: String) : Cd()
    data class Ls(val fileSize: Int) : Command()
}

object Day07Parser {
    fun List<String>.parseCommands(): List<Command> =
        mapNotNull {
            when {
                it.startsWith("\$ cd ") ->
                    it.replace("\$ cd ", "").let { cd ->
                        when (cd) {
                            ".." -> Up
                            else -> Down(cd)
                        }
                    }

                it.first().isDigit()    -> Ls((it.filter { it.isDigit() }.toIntOrNull() ?: 0))
                else                    -> null
            }
        }
}
