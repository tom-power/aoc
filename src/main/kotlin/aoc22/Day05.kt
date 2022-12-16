package aoc22

import aoc22.Collections.splitBy
import aoc22.Collections.transpose
import aoc22.Day05Runner.crane9000
import aoc22.Day05Runner.crane9001
import aoc22.Day05Domain.Movement
import aoc22.Day05Parser.toMovements
import aoc22.Day05Parser.toStacks

object Day05 : Day {
    fun List<String>.part1(): String = moveCratesWith(::crane9000)

    fun List<String>.part2(): String = moveCratesWith(::crane9001)
}

private fun List<String>.moveCratesWith(crane: (List<Stack>, List<Movement>) -> List<Stack>): String =
    splitBy("")
        .let { crane(it[0].dropLast(1).toStacks(), it[1].toMovements()) }
        .joinToString("") { it.last().toString() }

object Day05Runner {
    fun crane9000(stacks: List<Stack>, movements: List<Movement>): List<Stack> =
        craneWithOrder(stacks, movements, true)

    fun crane9001(stacks: List<Stack>, movements: List<Movement>): List<Stack> =
        craneWithOrder(stacks, movements, false)

    private fun craneWithOrder(stacks: List<Stack>, movements: List<Movement>, reversed: Boolean) =
        movements.fold(stacks.map { it.toMutableList() }) { acc, movement ->
            acc.apply {
                val from: Stack = acc[movement.from - 1].takeLastReversed(movement.number, reversed)
                acc[movement.to - 1].addAll(from)
                acc[movement.from - 1].removeLast(from.count())
            }
        }

    private fun Stack.takeLastReversed(n: Int, reversed: Boolean) =
        takeLast(n).toMutableList()
            .run { if (reversed) reversed() else this }

    private fun MutableList<Char>.removeLast(n: Int) {
        repeat(n) { this.removeLast() }
    }
}

typealias Stack = List<Char>

object Day05Domain {
    data class Movement(val number: Int, val from: Int, val to: Int)
}

object Day05Parser {
    fun List<String>.toStacks(): List<Stack> {
        val max = this.maxBy { it.count() }.count()
        return map { row ->
            row.padEnd(max, ' ').chunked(4).map {
                it.filter(Char::isUpperCase)
            }
        }
            .transpose()
            .map { row ->
                row.reversed()
                    .filter(String::isNotBlank)
                    .map { it[0] }
            }

    }

    fun List<String>.toMovements(): List<Movement> =
        map { it.split(" ").mapNotNull(String::toIntOrNull) }
            .map { Movement(it[0], it[1], it[2]) }
}
