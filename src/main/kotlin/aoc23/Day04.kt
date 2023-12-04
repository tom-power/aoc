package aoc23

import aoc23.Day04Domain.ScratchCards
import aoc23.Day04Parser.toScratchCards
import aoc23.Day04Solution.part1Day04
import aoc23.Day04Solution.part2Day04
import common.Year23
import kotlin.math.pow

object Day04 : Year23() {
    fun List<String>.part1(): Int = part1Day04()

    fun List<String>.part2(): Int = part2Day04()
}

object Day04Solution {
    fun List<String>.part1Day04(): Int =
        toScratchCards()
            .points()

    fun List<String>.part2Day04(): Int =
        toScratchCards()
            .cardsWon()
}

object Day04Domain {
    data class ScratchCards(
        private val cards: List<Card>
    ) {
        private val cardMatches = cards.map { it.matches() }

        fun points(): Int = cardMatches.sumOf { 2.0.pow(it - 1).toInt() }

        fun cardsWon(): Int {
            val cards = IntArray(cardMatches.size) { 1 }
            cardMatches.forEachIndexed { index, copies ->
                repeat(copies) { copyIndex ->
                    cards[index + copyIndex + 1] += cards[index]
                }
            }
            return cards.sum()
        }
    }

    data class Card(
        val winning: Set<Int>,
        val yours: Set<Int>
    ) {
        fun matches(): Int = winning.intersect(yours).size
    }
}

object Day04Parser {
    fun List<String>.toScratchCards(): ScratchCards =
        ScratchCards(
            cards = map { line ->
                line.split(":").let { (id, cards) ->
                    cards.split(" | ").let { (winning, yours) ->
                        Day04Domain.Card(
                            winning = winning.toInts(),
                            yours = yours.toInts()
                        )
                    }
                }
            }
        )

    private fun String.toInts(): Set<Int> = split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }.toSet()
}
