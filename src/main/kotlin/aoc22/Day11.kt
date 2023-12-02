package aoc22

import common.Collections.partitionedBy
import common.Collections.product
import aoc22.Day11Solution.part1Day11
import aoc22.Day11Solution.part2Day11
import aoc22.Day11Domain.Item
import aoc22.Day11Domain.Monkey
import aoc22.Day11Parser.toMonkeys
import aoc22.Day11Runner.doRounds
import common.Year22

object Day11: Year22() {
    fun List<String>.part1(): Long = part1Day11()

    fun List<String>.part2(): Long = part2Day11()
}

object Day11Solution {
    fun List<String>.part1Day11(): Long =
        toMonkeys(reliefDivisor = 3)
            .doRounds(times = 20)
            .productOfInspectedTimes(top = 2)

    fun List<String>.part2Day11(): Long =
        toMonkeys(reliefDivisor = 1)
            .doRounds(times = 10000)
            .productOfInspectedTimes(top = 2)

    private fun Monkeys.productOfInspectedTimes(top: Int): Long =
        map { it.inspectedTimes }
            .sortedBy { it }
            .takeLast(top)
            .product()
}

private typealias Monkeys = List<Monkey>

object Day11Runner {
    fun Monkeys.doRounds(times: Int): Monkeys =
        apply {
            repeat(times) {
                this.forEach { monkey ->
                    val itemsToThrow =
                        generateSequence {
                            monkey.items.removeFirstOrNull()?.let { item ->
                                with(monkey) {
                                    item
                                        .inspect()
                                        .getBored()
                                        .reduceWorryLevel(using = productOftestDivisibleBy())
                                        .let { inspected -> inspected to targetMonkeyFor(inspected) }
                                }
                            }
                        }
                    itemsToThrow.forEach { (item, targetMonkey) ->
                        this.throwItem(item = item, toMonkey = targetMonkey)
                    }
                }
            }
        }

    private fun Monkeys.throwItem(item: Item, toMonkey: Int) {
        this[toMonkey].items.add(item)
    }

    private fun Monkeys.productOftestDivisibleBy() = map { it.testDivisibleBy }.product()
}

object Day11Domain {
    data class Item(
        // only used with % testDivisibleBy to work out where to throw an item
        // reduce with % of testDivisibleBy lcm to make it a manageable size in part 2
        // continues to work with % testDivisibleBy because of maths
        val worryLevel: Long,
    )

    data class Monkey(
        val items: MutableList<Item>,
        val operationParts: List<String>,
        val testDivisibleBy: Int,
        val monkeyTrue: Int,
        val monkeyFalse: Int,
        var inspectedTimes: Long,
        val reliefDivisor: Int,
    ) {
        fun Item.inspect(): Item = this@Monkey.operation(this, operationParts).also { inspectedTimes++ }
        fun Item.getBored(): Item = this.copy(worryLevel = (this.worryLevel / reliefDivisor))

        fun Item.reduceWorryLevel(using: Int?): Item = using?.let { copy(worryLevel = this.worryLevel % it) } ?: this

        fun targetMonkeyFor(item: Item): Int = if (item.test()) monkeyTrue else monkeyFalse

        private fun Item.test(): Boolean = worryLevel % testDivisibleBy == 0L

        private fun operation(item: Item, operationParts: List<String>): Item {
            fun String.toLongWith(item: Item): Long = if (this == "old") item.worryLevel else this.toLong()

            val first = operationParts[0].toLongWith(item)
            val operator = operationParts[1]
            val second = operationParts[2].toLongWith(item)

            return when {
                operator == "*" -> first * second
                operator == "+" -> first + second
                else            -> error("dunno")
            }.let(::Item)
        }
    }
}

object Day11Parser {
    fun List<String>.toMonkeys(reliefDivisor: Int): Monkeys =
        partitionedBy("").map { monkeyText ->
            val trimmed = monkeyText.map(String::trim)
            Monkey(
                items = trimmed[1].replace("Starting items: ", "").split(", ").map { Item(it.toLong()) }.toMutableList(),
                operationParts = trimmed[2].replace("Operation: new = ", "").split(" "),
                testDivisibleBy = trimmed[3].replace("Test: divisible by ", "").toInt(),
                monkeyTrue = trimmed[4].replace("If true: throw to monkey ", "").toInt(),
                monkeyFalse = trimmed[5].replace("If false: throw to monkey ", "").toInt(),
                inspectedTimes = 0L,
                reliefDivisor = reliefDivisor,
            )
        }
}