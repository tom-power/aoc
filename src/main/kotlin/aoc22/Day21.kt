package aoc22

import aoc22.Day21Domain.HumanMonkey
import aoc22.Day21Domain.MathMonkey
import aoc22.Day21Domain.Monkey
import aoc22.Day21Domain.NumberMonkey
import aoc22.Day21Domain.Operator
import aoc22.Day21Domain.Operator.*
import aoc22.Day21Parser.toMonkeys
import aoc22.Day21Parser.toRootMonkey
import aoc22.Day21Runner.yellEquals
import aoc22.Day21Solution.part1Day21
import aoc22.Day21Solution.part2Day21
import common.Day

object Day21 : Day {
    fun List<String>.part1(): Long = part1Day21()

    fun List<String>.part2(): Long = part2Day21()
}

object Day21Solution {
    fun List<String>.part1Day21(): Long =
        toMonkeys()
            .toRootMonkey()
            .yell()

    fun List<String>.part2Day21(): Long =
        toMonkeys()
            .yellEquals()
}

object Day21Runner {
    fun List<Monkey>.yellEquals(): Long =
        (toRootMonkey() as MathMonkey)
            .let { it to it.pathToHuman() }
            .let { (rootMonkey, pathToHuman) ->
                listOf(rootMonkey.left, rootMonkey.right)
                    .partition { it in pathToHuman }
                    .let { (humanSide, monkeySide) ->
                        humanSide.first().findEquals(monkeySide.first().yell(), pathToHuman)
                    }
            }
}

object Day21Domain {
    interface Monkey {
        val name: String
        fun yell(): Long
        fun pathToHuman(): Set<Monkey>
        fun findEquals(incoming: Long, pathToHuman: Set<Monkey>): Long
    }

    enum class Operator { Plus, Minus, Divide, Multiply }

    data class MathMonkey(
        override val name: String,
        val left: Monkey,
        val right: Monkey,
        val operator: Operator,
    ) : Monkey {
        override fun yell(): Long = left.yell() operate right.yell()

        private infix fun Long.operate(other: Long): Long =
            when (operator) {
                Plus -> this + other
                Minus -> this - other
                Divide -> this / other
                Multiply -> this * other
            }

        override fun pathToHuman(): Set<Monkey> =
            listOf(
                left.pathToHuman(),
                right.pathToHuman()
            ).filter { it.isNotEmpty() }
                .flatMap { it + this }
                .toSet()

        override fun findEquals(incoming: Long, pathToHuman: Set<Monkey>): Long =
            listOf(left, right)
                .single { it in pathToHuman }
                .let { it.findEquals(negate(it, incoming), pathToHuman) }

        private fun negate(monkey: Monkey, incoming: Long): Long =
            when (monkey) {
                left -> incoming leftNegate right.yell()
                right -> incoming rightNegate left.yell()
                else -> error("monkey mismatch")
            }

        private infix fun Long.leftNegate(other: Long): Long =
            when (operator) {
                Plus -> this - other
                Minus -> this + other
                Multiply -> this / other
                Divide -> this * other
            }

        private infix fun Long.rightNegate(other: Long): Long =
            when (operator) {
                Plus -> this - other
                Minus -> other - this
                Multiply -> this / other
                Divide -> other / this
            }
    }

    data class NumberMonkey(
        override val name: String,
        val number: Long,
    ) : Monkey {
        override fun yell(): Long = number
        override fun pathToHuman(): Set<Monkey> = emptySet()
        override fun findEquals(incoming: Long, pathToHuman: Set<Monkey>): Long = number
    }

    data class HumanMonkey(
        override val name: String,
        val number: Long,
    ) : Monkey {
        override fun yell(): Long = number
        override fun pathToHuman(): Set<Monkey> = setOf(this)
        override fun findEquals(incoming: Long, pathToHuman: Set<Monkey>): Long = incoming
    }
}

private typealias NamedMonkey = Pair<String, String>

object Day21Parser {
    private fun String.toOperator(): Operator = mapNotNull { it.toOperator() }.first()
    private fun Char.toOperator(): Operator? =
        when (this) {
            '+' -> Plus
            '-' -> Minus
            '/' -> Divide
            '*' -> Multiply
            else -> null
        }

    private fun String.toLeftName(): String = filter { it.isLetter() }.take(4)
    private fun String.toRightName(): String = filter { it.isLetter() }.takeLast(4)

    private fun List<NamedMonkey>.getMonkeyBy(name: String): Monkey = this.first { it.first == name }.toMonkey()

    private fun List<String>.toNamedMonkeys(): List<NamedMonkey> =
        map { row -> row.split(":").let { Pair(it[0], it[1]) } }

    context(List<NamedMonkey>)
    private fun NamedMonkey.toMonkey(): Monkey =
        this.let { (name, rest) ->
            val number = rest.filter { it.isDigit() }
            when {
                number.isNotEmpty() -> when (name) {
                    "humn" -> HumanMonkey(name, number.toLong())
                    else -> NumberMonkey(name, number.toLong())
                }

                else -> MathMonkey(
                    name = name,
                    operator = rest.toOperator(),
                    left = this@List.getMonkeyBy(rest.toLeftName()),
                    right = this@List.getMonkeyBy(rest.toRightName()),
                )
            }
        }

    fun List<String>.toMonkeys(): List<Monkey> =
        toNamedMonkeys().run {
            this.map { it.toMonkey() }
        }

    fun List<Monkey>.toRootMonkey(): Monkey = first { it.name == "root" }

}