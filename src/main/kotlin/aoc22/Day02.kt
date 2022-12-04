package aoc22

import aoc22.Common.summed
import aoc22.Common.theirPlayMap
import aoc22.Common.toRoundsUsing
import aoc22.Domain.Outcome
import aoc22.Domain.Outcome.*
import aoc22.Domain.Play
import aoc22.Domain.Play.*
import aoc22.Domain.Round
import aoc22.Domain.points
import aoc22.Domain.toOutcome

object Day02 : Day {
    override fun List<String>.part1(): Int = toRoundsUsing(Part1::toPlays).summed()

    override fun List<String>.part2(): Int = toRoundsUsing(Part2::toPlaysForOutcome).summed()
}

object Domain {
    enum class Play {
        Rock, Paper, Scissors
    }

    fun Play.points(): Int =
        when (this) {
            Rock     -> 1
            Paper    -> 2
            Scissors -> 3
        }

    private fun Play.beats(): Play =
        when (this) {
            Rock     -> Scissors
            Paper    -> Rock
            Scissors -> Paper
        }

    data class Round(val theirPlay: Play, val yourPlay: Play)

    fun Round.toOutcome(): Outcome =
        when {
            yourPlay.beats() == theirPlay -> Win
            yourPlay == theirPlay         -> Draw
            else                          -> Lose
        }

    enum class Outcome { Win, Lose, Draw }

    fun Outcome.points(): Int =
        when (this) {
            Win  -> 6
            Draw -> 3
            Lose -> 0
        }
}

object Common {
    val theirPlayMap = mapOf("A" to Rock, "B" to Paper, "C" to Scissors)

    private fun List<String>.toKeys(): Keys = map { it.split(" ").run { this[0] to this[1] } }

    private fun Pair<Play, Play>.toRound() = Round(first, second)

    fun List<String>.toRoundsUsing(toPlays: Keys.() -> Plays): Rounds = toKeys().toPlays().map { it.toRound() }

    fun List<Round>.summed() = map { it.toOutcome().points() + it.yourPlay.points() }.sum()
}

typealias Keys = List<Pair<String, String>>
typealias Plays = List<Pair<Play, Play>>
typealias Rounds = List<Round>

object Part1 {
    private val yourPlayMap = mapOf("X" to Rock, "Y" to Paper, "Z" to Scissors)

    fun toPlays(keys: Keys): Plays =
        keys.map { (theirKey, yourKey) ->
            theirPlayMap[theirKey]!! to yourPlayMap[yourKey]!!
        }
}

object Part2 {
    private val yourOutcomeMap = mapOf("X" to Lose, "Y" to Draw, "Z" to Win)

    fun toPlaysForOutcome(keys: Keys): Plays =
        keys.map { (theirKey, yourKey) ->
            theirPlayMap[theirKey]!!.let { theirPlay ->
                theirPlay to theirPlay.playFor(yourOutcomeMap[yourKey]!!)
            }
        }

    private fun Play.playFor(outcome: Outcome): Play =
        Play.values().first { Round(this, it).toOutcome() == outcome }
}
