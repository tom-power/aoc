package aoc23

import aoc23.Day19Domain.Accepted
import common.Misc.log
import common.Year23
import aoc23.Day19Domain.Aplenty
import aoc23.Day19Domain.Part
import aoc23.Day19Domain.Rating
import aoc23.Day19Domain.RatingCategory
import aoc23.Day19Domain.Rejected
import aoc23.Day19Domain.Rule
import aoc23.Day19Domain.Transitional
import aoc23.Day19Parser.toAplenty
import common.Collections.partitionedBy
import common.Space3D.Parser.valuesFor

object Day19 : Year23 {
    fun List<String>.part1(): Int =
        toAplenty()
            .apply { sortParts() }
            .sumOfAcceptedRatings()

    fun List<String>.part2(): Int =
        toAplenty()
            .log()
            .let { 0 }
}

object Day19Domain {
    class Aplenty(
        private val workflows: Map<String, Workflow>,
        private val parts: List<Part>,
    ) {
        private var accepted = listOf<Part>()

        fun sortParts() {
            accepted = parts.mapNotNull { sortThisPart(it, workflows["in"]!!) }
        }

        private fun sortThisPart(part: Part, workflow: Workflow): Part? =
            when (workflow) {
                Accepted -> part
                Rejected -> null
                is Transitional -> {
                    sortThisPart(part, workflows[workflow.match(part)]!!)
                }
            }

        fun sumOfAcceptedRatings(): Int =
            accepted
                .flatMap { part -> part.ratings.map { it.value } }
                .sum()
    }

    sealed interface Workflow

    class Rule(
        private val ratingCategory: RatingCategory,
        private val ltgt: Char,
        private val comparator: Int,
        private val destination: String
    ) : (Rating) -> String? {
        override fun invoke(rating: Rating): String? =
            when {
                rating.category == ratingCategory ->
                    destination.takeIf {
                        when {
                            ltgt == '<' -> rating.value < comparator
                            ltgt == '>' -> rating.value > comparator
                            else -> error("bad")
                        }
                    }

                else -> null
            }
    }

    class Transitional(
        private val rules: List<(Rating) -> String?>,
        private val unMatchedWorkflow: String,
    ) : Workflow {
        fun match(part: Part): String =
            rules.firstNotNullOfOrNull { rule ->
                part.ratings.firstNotNullOfOrNull { rule(it) }
            } ?: unMatchedWorkflow
    }

    sealed interface Terminal : Workflow
    data object Accepted : Terminal
    data object Rejected : Terminal

    enum class RatingCategory { x, m, a, s }

    class Rating(
        val category: RatingCategory,
        val value: Int,
    )

    class Part(
        val ratings: List<Rating>
    )
}

object Day19Parser {
    fun List<String>.toAplenty(): Aplenty {
        val partitionedBy = this.partitionedBy("")
        val workflows = partitionedBy.first()
        val parts = partitionedBy.last()
        return Aplenty(
            workflows =
            workflows
                .associate {
                    it.dropLast(1).split("{").run {
                        first() to last().toTransistional()
                    }
                } +
                mapOf(
                    "A" to Accepted,
                    "R" to Rejected,
                ),
            parts = parts.map { it.toPart() }
        )
    }

    private fun String.toTransistional(): Transitional {
        val split = this.split(",")
        return Transitional(
            rules = split.dropLast(1).map { it.toRule() },
            unMatchedWorkflow = split.last()
        )
    }

    private val ruleRegex = """(\w)([<>])(\d+):(\w+)""".toRegex()

    private fun String.toRule(): Rule {
        val (ratingCategory, ltgt, comparator, destination) = ruleRegex.valuesFor(this)
        return Rule(
            ratingCategory = RatingCategory.valueOf(ratingCategory),
            ltgt = ltgt[0],
            comparator = comparator.toInt(),
            destination = destination,
        )
    }

    private fun String.toPart(): Part =
        Part(
            ratings =
            this
                .drop(1)
                .dropLast(1)
                .split(",")
                .map { it.toRating() }
        )

    private fun String.toRating(): Rating {
        val (category, value) = this.split("=")
        return Rating(
            category = RatingCategory.valueOf(category),
            value = value.toInt()
        )
    }
}
