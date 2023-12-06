package aoc22

import aoc22.Day25Domain.BaseConvertor.toBalancedQuinary
import aoc22.Day25Domain.BaseConvertor.toDecimal
import aoc22.Day25Domain.SnafuConvertor.toBalancedQuinary
import aoc22.Day25Domain.SnafuConvertor.toSnafu
import aoc22.Day25Solution.part1Day25
import common.Year22
import kotlin.math.pow

object Day25: Year22 {
    fun List<Snafu>.part1(): Snafu = part1Day25()
}

object Day25Solution {
    fun List<Snafu>.part1Day25(): Snafu =
        map { snafu -> snafu.toBalancedQuinary().toDecimal() }.sum()
            .toBalancedQuinary()
            .toSnafu()
}

private typealias Decimal = Long
private typealias BalancedQuinary = List<Int>
private typealias Snafu = String

object Day25Domain {
    object BaseConvertor {
        fun BalancedQuinary.toDecimal(): Decimal =
            reversed().foldIndexed(0L) { index, acc, i ->
                acc +
                    when (index) {
                        0 -> i
                        else -> 5.toDouble().pow(index) * i
                    }.toLong()
            }

        fun Decimal.toBalancedQuinary(): BalancedQuinary =
            generateSequence(this) { (it + 2) / 5 }.takeWhile { it != 0L }
                .map {
                    when(val mod = (it % 5).toInt()) {
                        3 -> -2
                        4 -> -1
                        else -> mod
                    }
                }
                .toList()
                .reversed()
    }

    object SnafuConvertor {
        private val snafuIntMap =
            mapOf(
                '=' to -2,
                '-' to -1,
                '0' to 0,
                '1' to 1,
                '2' to 2,
            )

        fun Snafu.toBalancedQuinary(): BalancedQuinary = map { snafuIntMap[it]!! }

        fun BalancedQuinary.toSnafu(): Snafu =
            map { int -> snafuIntMap.filter { it.value == int }.keys.single() }
                .joinToString("")
    }
}
