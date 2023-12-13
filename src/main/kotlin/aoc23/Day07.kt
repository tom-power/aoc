package aoc23

import common.Year23
import aoc23.Day07Domain.CamelCards
import aoc23.Day07Domain.Cards
import aoc23.Day07Parser.toCamelCards
import aoc23.Day07Solution.part1Day07
import aoc23.Day07Solution.part2Day07
import com.github.shiguruikai.combinatoricskt.permutationsWithRepetition
import common.Strings.replaceAt

object Day07 : Year23 {
    fun List<String>.part1(): Int = part1Day07()

    fun List<String>.part2(): Int = part2Day07()
}

object Day07Solution {
    fun List<String>.part1Day07(): Int {
        Day07Domain.withJoker = false
        return toCamelCards()
            .toTotalWinnings()
    }


    fun List<String>.part2Day07(): Int {
        Day07Domain.withJoker = true
        return toCamelCards()
            .toTotalWinnings()
    }
}

object Day07Domain {
    var withJoker: Boolean = false

    class CamelCards(
        val hands: List<Hand>
    ) {
        fun toTotalWinnings(): Int =
            hands.sorted().foldIndexed(0) { index, acc, hand ->
                acc + hand.bid * (index + 1)
            }
    }

    private val cardsByValue: String
        get() =
            when {
                withJoker -> "J23456789TQKA"
                else -> "23456789TJQKA"
            }

    class Hand(
        val cards: Cards,
        val bid: Int,
    ) : Comparable<Hand> {
        private val type: Int =
            with(JokerReplacer) {
                cards
                    .replaceJoker()
                    .type
            }

        override fun compareTo(other: Hand): Int =
            type.compareTo(other.type)
                .takeUnless { it == 0 }
                ?: tieBreak(other)

        private fun tieBreak(other: Hand): Int =
            this.cards
                .value
                .mapIndexed { index, c -> c to other.cards.value[index] }
                .map { Card(it.first).compareTo(Card(it.second)) }
                .firstOrNull { it != 0 }
                ?: 0
    }

    object JokerReplacer {
        fun Cards.replaceJoker(): Cards =
            if (withJoker) {
                toReplacedJokers()
                    .filterMaxType()
                    .last()
            } else this

        private fun Cards.toReplacedJokers(): List<Cards> {
            val jokerIndexes =
                this.value.toList().mapIndexedNotNull { index, c ->
                    index.takeIf { c == 'J' }
                }

            val cardPermutations =
                cardsByValue.toList().permutationsWithRepetition(jokerIndexes.size).toList()

            return cardPermutations.map { replacements ->
                Cards(
                    value =
                    jokerIndexes.foldIndexed(this.value) { index, acc, jokerIndex ->
                        acc.replaceAt(jokerIndex, replacements[index])
                    }
                )
            }
        }

        private fun List<Cards>.filterMaxType(): List<Cards> =
            this.maxOf { it.type }.let { maxType ->
                this.filter { it.type == maxType }
            }
    }

    class Card(private val char: Char) : Comparable<Card> {
        override fun compareTo(other: Card): Int =
            this.char.value.compareTo(other.char.value)

        private val Char.value: Int
            get() = cardsByValue.indexOf(this)
    }

    class Cards(
        val value: String,
    ) {
        val type: Int by lazy {
            value.groupBy { it }.values
                .filter { type -> type.size > 1 }
                .let { scoringTypes ->
                    val matchesSize =
                        scoringTypes
                            .sumOf { type -> type.size } * 3
                    val numberOfMatches = scoringTypes.size * 4
                    matchesSize - numberOfMatches
                }
        }
    }
}

object Day07Parser {
    fun List<String>.toCamelCards(): CamelCards =
        CamelCards(
            hands = this.map { line ->
                line.split(" ").let { (cards, bid) ->
                    Day07Domain.Hand(
                        cards = Cards(cards),
                        bid = bid.toInt(),
                    )
                }
            }
        )
}
