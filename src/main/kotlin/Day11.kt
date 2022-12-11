package aoc22

import aoc22.Collections.partitionedBy
import aoc22.Collections.product
import aoc22.Day11Solution.part1Day11
import aoc22.Day11Solution.part2Day11
import aoc22.Misc.log
import aoc22.MonkeyDomain.Item
import aoc22.MonkeyDomain.Monkey

object Day11 : Day<String, Int, Int> {
    override fun List<String>.part1(): Int = part1Day11()

    override fun List<String>.part2(): Int = part2Day11()
}

object Day11Solution {
    fun List<String>.part1Day11(): Int =
        toMonkeys()
            .doRounds(rounds = 20)
            .map { it.inspectedTimes }
            .sortedBy { it }
            .takeLast(2)
            .product()

    fun List<String>.part2Day11(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

private fun Monkeys.doRounds(rounds: Int): Monkeys =
    (0 until rounds).fold(this) { monkeys, _ ->
        monkeys.forEach { monkey ->
            generateSequence {
                monkey.items.removeFirstOrNull()?.let {
                    with(monkey) {
                        it.inspect().getBored().let { inspected ->
                            inspected to inspected.forMonkey()
                        }
                    }
                }
            }.forEach { monkeys.throwTo(item = it.first, monkeyTo = it.second) }
        }
        monkeys
    }

private fun List<Monkey>.throwTo(item: Item, monkeyTo: Int) {
    this[monkeyTo].items.add(item)
}

private fun List<String>.toMonkeys(): Monkeys =
    partitionedBy("").map { monkeyText ->
        val trimmed = monkeyText.map(String::trim)
        Monkey(
            items = trimmed[1].replace("Starting items: ", "").split(", ").map { Item(it.toInt()) }.toMutableList(),
            operationParts = trimmed[2].replace("Operation: new = ", "").split(" "),
            divisor = trimmed[3].replace("Test: divisible by ", "").toInt(),
            monkeyTrue = trimmed[4].replace("If true: throw to monkey ", "").toInt(),
            monkeyFalse = trimmed[5].replace("If false: throw to monkey ", "").toInt(),
            inspectedTimes = 0,
        )
    }

typealias Monkeys = List<Monkey>

object MonkeyDomain {
    data class Item(
        val worryLevel: Int,
    )

    data class Monkey(
        val items: MutableList<Item>,
        val operationParts: List<String>,
        val divisor: Int,
        val monkeyTrue: Int,
        val monkeyFalse: Int,
        var inspectedTimes: Int,
    ) {
        fun Item.inspect(): Item = this@Monkey.operation(this, operationParts).also { inspectedTimes++ }
        fun Item.getBored(): Item = this.copy(worryLevel = (this.worryLevel / 3))


        fun Item.forMonkey(): Int = if (test()) monkeyTrue else monkeyFalse
        private fun Item.test(): Boolean = worryLevel % divisor == 0

        private fun operation(item: Item, operationParts: List<String>): Item {
            fun String.toIntWith(item: Item): Int = if (this == "old") item.worryLevel else this.toInt()

            val first = operationParts[0].toIntWith(item)
            val operator = operationParts[1]
            val second = operationParts[2].toIntWith(item)

            return when {
                operator == "*" -> first * second
                operator == "+" -> first + second
                else            -> error("dunno")
            }.let { Item(it) }
        }
    }
}