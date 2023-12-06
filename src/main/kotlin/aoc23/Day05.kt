package aoc23

import aoc23.Day05Domain.Almanac
import aoc23.Day05Domain.SeedMapper
import aoc23.Day05Parser.toSeedMapper
import aoc23.Day05Solution.part1Day05
import aoc23.Day05Solution.part2Day05
import common.Year23

object Day05 : Year23 {
    fun List<String>.part1(): Long = part1Day05()

    fun List<String>.part2(): Long = part2Day05()
}

object Day05Solution {
    fun List<String>.part1Day05(): Long =
        toSeedMapper()
            .findLowestLocationSeed()

    fun List<String>.part2Day05(): Long =
        toSeedMapper()
            .findLowestLocationSeedRange()
}

typealias Mapper = List<Day05Domain.FromToMap>

object Day05Domain {
    data class SeedMapper(
        val seeds: List<Long>,
        val almanac: Almanac,
    ) {
        fun findLowestLocationSeed(): Long =
            seeds.minOf { seed ->
                almanac.mapFromTo(seed)
            }

        private val seedsRanges: List<LongRange> =
            seeds.chunked(2)
                .map { it.first()..it.first() + (it.last() - 1) }

        private val locations: Sequence<Long> = generateSequence(1L) { it + 1 }

        private val reversedAlmanac: Almanac by lazy {
            Almanac(
                mappers =
                almanac.mappers
                    .reversed()
                    .map { mapper ->
                        mapper
                            .reversed()
                            .map {
                                it.copy(fromStart = it.toStart, toStart = it.fromStart)
                            }
                    }
            )
        }

        fun findLowestLocationSeedRange(): Long =
            locations.first { location ->
                reversedAlmanac.mapFromTo(location).let { seed ->
                    seedsRanges.any { seedRange -> seed in seedRange }
                }
            }
    }

    data class Almanac(
        val mappers: List<Mapper>,
    ) {
        fun mapFromTo(seed: Long): Long =
            mappers.fold(seed) { acc, mapper ->
                mapper
                    .firstNotNullOfOrNull { map -> map.toOrNull(acc) } ?: acc
            }
    }

    data class FromToMap(
        val fromStart: Long,
        val toStart: Long,
        val range: Long
    ) {
        private val fromRange = fromStart..(fromStart + range)
        private val fromToDelta = toStart - fromStart

        fun toOrNull(from: Long): Long? =
            when {
                from in fromRange -> from + fromToDelta

                else -> null
            }
    }
}

object Day05Parser {
    fun List<String>.toSeedMapper(): SeedMapper =
        SeedMapper(
            seeds = parseSeeds(),
            almanac = Almanac(
                mappers = parseMappers()
            )
        )

    private fun List<String>.parseMappers(): List<Mapper> =
        this.joinToString(System.lineSeparator())
            .split("${System.lineSeparator()}${System.lineSeparator()}")
            .drop(1)
            .map { mapperString ->
                val fromTos =
                    mapperString
                        .split(":")
                        .last()
                        .split(System.lineSeparator())
                        .filter { it.isNotBlank() }
                fromTos
                    .map { fromTo -> fromTo.parseFromToMap() }

            }

    private fun String.parseFromToMap(): Day05Domain.FromToMap {
        val numbers = split(" ").map { it.toLong() }
        return Day05Domain.FromToMap(
            fromStart = numbers[1],
            toStart = numbers[0],
            range = numbers[2],
        )
    }

    private fun List<String>.parseSeeds(): List<Long> =
        first()
            .split(":")
            .last()
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }

}
