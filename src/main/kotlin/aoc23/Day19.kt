package aoc23

import aoc23.Day19Domain.Accepted
import common.Year23
import aoc23.Day19Domain.Aplenty
import aoc23.Day19Domain.Part
import aoc23.Day19Domain.Rating
import aoc23.Day19Domain.RatingCategory
import aoc23.Day19Domain.Rejected
import aoc23.Day19Domain.Rule
import aoc23.Day19Domain.Transitional
import aoc23.Day19Domain.WorkflowKey
import aoc23.Day19Parser.toAplenty
import common.Collections.partitionedBy
import common.Collections.product
import common.Space3D.Parser.valuesFor

object Day19 : Year23 {
    fun List<String>.part1(): Int =
        toAplenty()
            .apply { sortParts() }
            .sumOfAcceptedRatings()

    fun List<String>.part2(overrideParts: List<Part>? = null): Long =
        toAplenty(overrideParts = overrideParts ?: rangeParts())
            .apply { sortParts() }
            .countOfCombinationsOfAccepted()

    private fun rangeParts(): List<Part> =
        listOf(
            Part(
                ratings = RatingCategory.entries.map { Rating(it, IntRange(1, 4000)) }
            )
        )
}

object Day19Domain {
    class Aplenty(
        private val workflows: Map<String, Workflow>,
        private val parts: List<Part>,
    ) {
        private var accepted = listOf<Part>()

        fun sumOfAcceptedRatings(): Int =
            accepted
                .sumOf(Part::ratings)

        fun countOfCombinationsOfAccepted(): Long =
            accepted
                .sumOf(Part::combinations)

        fun sortParts() {
            accepted = parts.flatMap { sortThisPart(it, workflows["in"]!!) }.filterNotNull()
        }

        private fun sortThisPart(part: Part, workflow: Workflow): List<Part?> =
            when (workflow) {
                Accepted -> listOf(part)
                Rejected -> listOf(null)
                is Transitional -> {
                    workflow.match(part).flatMap { (part, nextWorkflow) ->
                        sortThisPart(part, workflows[nextWorkflow.value]!!)
                    }
                }
            }
    }

    @JvmInline
    value class WorkflowKey(val value: String)

    sealed interface Workflow

    class Transitional(
        private val key: WorkflowKey,
        private val rules: List<Rule>,
        private val unMatchedWorkflow: WorkflowKey,
    ) : Workflow {
        fun match(part: Part): List<Pair<Part, WorkflowKey>> {
            val maybeMatches =
                rules.firstNotNullOfOrNull { rule ->
                    rule.checkPart(part)
                }

            return maybeMatches ?: listOf(part to unMatchedWorkflow)
        }

        private fun Rule.checkPart(part: Part): List<Pair<Part, WorkflowKey>>? =
            part.ratings
                .filter { it.category == ratingCategory }
                .firstNotNullOfOrNull { rating ->
                    invoke(rating)?.map { (newRating, workflow) ->
                        (part.withRating(newRating) to workflow)
                    }
                }
    }

    enum class LtGt { Lt, Gt }

    class Rule(
        val ratingCategory: RatingCategory,
        private val ltgt: LtGt,
        private val comparator: Int,
        private val current: WorkflowKey,
        private val destination: WorkflowKey
    ) : (Rating) -> List<Pair<Rating, WorkflowKey>>? {
        override fun invoke(rating: Rating): List<Pair<Rating, WorkflowKey>>? =
            when (ltgt) {
                LtGt.Lt -> rating.nextFor { it < comparator }
                LtGt.Gt -> rating.nextFor { it > comparator }
            }

        private fun Rating.nextFor(predicate: (Int) -> Boolean): List<Pair<Rating, WorkflowKey>>? =
            this.range.partition(predicate).let { (match, noMatch) ->
                listOfNotNull(
                    this.toPair(match, destination),
                    this.toPair(noMatch, current)
                ).takeIf { match.isNotEmpty() }
            }

        private fun Rating.toPair(ints: List<Int>, workflowKey: WorkflowKey): Pair<Rating, WorkflowKey>? =
            ints
                .takeIf { it.isNotEmpty() }
                ?.let {
                    Pair(
                        this.copy(range = IntRange(it.min(), it.max())),
                        workflowKey
                    )
                }
    }

    sealed interface Terminal : Workflow
    data object Accepted : Terminal
    data object Rejected : Terminal

    enum class RatingCategory { x, m, a, s }

    data class Rating(
        val category: RatingCategory,
        val range: IntRange,
    )

    data class Part(
        val ratings: List<Rating>
    ) {
        fun ratings(): Int =
            ratings
                .sumOf { it.range.sum() }

        fun combinations(): Long =
            ratings
                .map { (it.range.last() - it.range.first()) + 1L }
                .product()

        fun withRating(rating: Rating): Part {
            val index = this@Part.ratings.indexOfFirst { it.category == rating.category }

            return Part(
                ratings =
                ratings.toMutableList()
                    .apply { set(index, rating) }
            )
        }
    }
}

object Day19Parser {
    fun List<String>.toAplenty(overrideParts: List<Part>? = null): Aplenty {
        val partitionedBy = this.partitionedBy("")
        val workflows = partitionedBy.first()
        val parts = partitionedBy.last()
        return Aplenty(
            workflows =
            workflows
                .associate {
                    it.dropLast(1).split("{").run {
                        first() to last().toTransistional(first())
                    }
                } +
                mapOf(
                    "A" to Accepted,
                    "R" to Rejected,
                ),
            parts = overrideParts ?: parts.map { it.toPart() }
        )
    }

    private fun String.toTransistional(key: String): Transitional {
        val split = this.split(",")
        val current = WorkflowKey(key)
        return Transitional(
            key = current,
            rules = split.dropLast(1).map { it.toRule(current) },
            unMatchedWorkflow = WorkflowKey(split.last())
        )
    }

    private val ruleRegex = """(\w)([<>])(\d+):(\w+)""".toRegex()

    private fun String.toRule(current: WorkflowKey): Rule {
        val (ratingCategory, ltgt, comparator, destination) = ruleRegex.valuesFor(this)
        return Rule(
            ratingCategory = RatingCategory.valueOf(ratingCategory),
            ltgt =
            when (ltgt[0]) {
                '<' -> Day19Domain.LtGt.Lt
                '>' -> Day19Domain.LtGt.Gt
                else -> error("bad")
            },
            comparator = comparator.toInt(),
            current = current,
            destination = WorkflowKey(destination),
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
            range = IntRange(value.toInt(), value.toInt())
        )
    }
}
