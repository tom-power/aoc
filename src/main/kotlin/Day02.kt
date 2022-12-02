import Common.summed
import Common.theirMapPlays
import Common.toKeys
import Common.toRound
import Domain.Outcome
import Domain.Outcome.*
import Domain.Play
import Domain.Play.*
import Domain.Round
import Domain.points
import Domain.toOutcome
import Part1.toRounds
import Part2.toKnownOutcomeRounds

object Day02 : Day {
    override fun List<String>.part1(): Int = toRounds().summed()

    override fun List<String>.part2(): Int = toKnownOutcomeRounds().summed()
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
    val theirMapPlays = mapOf("A" to Rock, "B" to Paper, "C" to Scissors)

    fun List<String>.toKeys() = map { it.split(" ").run { this[0] to this[1] } }

    fun Pair<Play, Play>.toRound() = Round(first, second)

    fun List<Round>.summed() = map { it.toOutcome().points() + it.yourPlay.points() }.sum()
}

object Part1 {
    fun List<String>.toRounds() = toKeys().toPlays().map { it.toRound() }

    private val yourMapPlays = mapOf("X" to Rock, "Y" to Paper, "Z" to Scissors)

    private fun List<Pair<String, String>>.toPlays() = map { theirMapPlays[it.first]!! to yourMapPlays[it.second]!! }
}

object Part2 {
    fun List<String>.toKnownOutcomeRounds() = toKeys().toKnownOutcomePlays().map { it.toRound() }

    private val yourMapOutcome = mapOf("X" to Lose, "Y" to Draw, "Z" to Win)

    private fun List<Pair<String, String>>.toKnownOutcomePlays() =
        map { theirMapPlays[it.first]!!.run { this to this.playFor(yourMapOutcome[it.second]!!) } }

    private fun Play.playFor(outcome: Outcome): Play =
        Play.values().first { Round(this, it).toOutcome() == outcome }
}
