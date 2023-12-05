package aoc23

import aoc23.Day05Domain.Almanac
import aoc23.Day05Domain.Mapper
import aoc23.Day05Domain.SeedMapper
import aoc23.Day05Domain.SourceDestMap
import common.Year23
import aoc23.Day05Parser.toSeedMapper
import aoc23.Day05Solution.part1Day05
import aoc23.Day05Solution.part2Day05

object Day05 : Year23() {
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

object Day05Domain {
    data class SeedMapper(
        val seeds: List<Long>,
        val almanac: Almanac,
    ) {
        private val seedsRanges: List<LongRange> =
            seeds.chunked(2)
                .map { it.first()..it.first() + (it.last() - 1) }

        private val locations: Sequence<Long> = generateSequence(1L) { it + 1 }

        fun findLowestLocationSeedRange(): Long =
            locations
                .first { location ->
                    almanac.seedFrom(location).let { seed ->
                        seedsRanges.any { seedRange -> seed in seedRange }
                    }
                }

        fun findLowestLocationSeed(): Long =
            seeds.minOf { seed ->
                almanac.locationFrom(seed)
            }
    }

    data class Almanac(
        val mappers: List<Mapper>,
    ) {
        fun locationFrom(seed: Long): Long =
            mappers.fold(seed) { acc, mapper ->
                mapper.mapToDestOrNull(acc) ?: acc
            }

        fun seedFrom(location: Long): Long =
            mappers.reversed().fold(location) { acc, mapper ->
                mapper.mapToSourceOrNull(acc) ?: acc
            }
    }

    data class Mapper(
        val sourceDestMaps: List<SourceDestMap>
    ) {
        fun mapToDestOrNull(source: Long): Long? =
            sourceDestMaps
                .firstNotNullOfOrNull { map -> map.toDestOrNull(source) }

        fun mapToSourceOrNull(dest: Long): Long? =
            sourceDestMaps
                .reversed()
                .firstNotNullOfOrNull { map -> map.toSourceOrNull(dest) }
    }

    data class SourceDestMap(
        val destinationStart: Long,
        val sourceStart: Long,
        val range: Long
    ) {
        private val sourceRange =  sourceStart..(sourceStart + range)
        private val sourceToDest = destinationStart - sourceStart

        fun toDestOrNull(source: Long): Long? =
            when {
                (source in sourceRange) -> {
                    source + sourceToDest
                }

                else -> null
            }

        private val destRange = destinationStart..(destinationStart + range)
        private val destToSource = sourceStart - destinationStart

        fun toSourceOrNull(dest: Long): Long? =
            when {
                (dest in destRange) -> {
                    dest + destToSource
                }

                else -> null
            }

    }
}

object Day05Parser {
    fun List<String>.toSeedMapper(): SeedMapper {
        return SeedMapper(
            seeds =
            first()
                .split(":")
                .last()
                .split(" ")
                .filter { it.isNotBlank() }
                .map {
                    it.trim().toLong()
                },
            almanac = Almanac(
                mappers =
                this.joinToString(System.lineSeparator())
                    .split("${System.lineSeparator()}${System.lineSeparator()}")
                    .drop(1)
                    .map { convertor ->
                        val sourceDests = convertor.split(":").last().split(System.lineSeparator()).filter { it.isNotBlank() }
                        Mapper(
                            sourceDestMaps =
                            sourceDests
                                .map { sourceDest ->
                                    val numbers = sourceDest.split(" ").map { it.toLong() }
                                    SourceDestMap(
                                        destinationStart = numbers[0],
                                        sourceStart = numbers[1],
                                        range = numbers[2],
                                    )
                                }
                        )
                    }
            )
        )
    }

}
