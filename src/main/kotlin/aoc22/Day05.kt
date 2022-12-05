package aoc22

import aoc22.Crane.crane9000
import aoc22.Crane.crane9001
import aoc22.Parser.Movement
import aoc22.Parser.toMovements
import aoc22.Parser.toStacks

object Day05 : Day<String, String> {
    override fun List<String>.part1(): String = moveCratesWith(::crane9000)

    override fun List<String>.part2(): String = moveCratesWith(::crane9001)
}

private fun List<String>.moveCratesWith(crane: (List<Stack>, List<Movement>) -> List<Stack>): String =
    splitBy("")
        .let { crane(it[0].dropLast(1).toStacks(), it[1].toMovements()) }
        .joinToString("") { it.last().toString() }

typealias Stack = List<Char>

object Crane {
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

object Parser {
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

    data class Movement(val number: Int, val from: Int, val to: Int)

    fun List<String>.toMovements(): List<Movement> =
        map { it.split(" ").mapNotNull(String::toIntOrNull) }
            .map { Movement(it[0], it[1], it[2]) }
}
