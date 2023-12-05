package aoc23

import aoc23.Day05Domain.Almanac
import aoc23.Day05Domain.Convertor
import aoc23.Day05Domain.SeedMapper
import aoc23.Day05Domain.SourceDestMap
import common.Misc.log
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
            .toLocations()
            .min()

    fun List<String>.part2Day05(): Long =
        toSeedMapper().log()
            .let { 0 }
}

object Day05Domain {
    data class SeedMapper(
        val seeds: List<Long>,
        val almanac: Almanac,
    ) {
        fun toLocations(): List<Long> =
            seeds.map { seed ->
                almanac.convertors.fold(seed) { acc, c ->
                    c.invoke(acc) ?: acc
                }
            }
    }

    data class Almanac(
        val convertors: List<Convertor>
    )

    data class Convertor(
        val name: String,
        val sourceDestMaps: List<SourceDestMap>
    ) : (Long) -> Long? {
        override fun invoke(source: Long): Long? =
            sourceDestMaps
                .firstOrNull { it.inRange(source) }
                ?.let { it.toDest(source) }
    }

    data class SourceDestMap(
        val sourceStart: Long,
        val destinationStart: Long,
        val range: Long
    ) {
        fun inRange(source: Long): Boolean = source in sourceStart..(sourceStart + range)
        fun toDest(source: Long): Long = source + (destinationStart - sourceStart)

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
                convertors =
                this.joinToString(System.lineSeparator())
                    .split("${System.lineSeparator()}${System.lineSeparator()}")
                    .drop(1)
                    .map { convertor ->
                        val sourceDests = convertor.split(":").last().split(System.lineSeparator()).filter { it.isNotBlank() }
                        Convertor(
                            name = "",
                            sourceDestMaps =
                            sourceDests
                                .map { sourceDest ->
                                    val numbers = sourceDest.split(" ").map { it.toLong() }
                                    SourceDestMap(
                                        sourceStart = numbers[1],
                                        destinationStart = numbers[0],
                                        range = numbers[2],
                                    )
                                }
                        )
                    }
            )
        )
    }

}
