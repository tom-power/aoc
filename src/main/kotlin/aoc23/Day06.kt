package aoc23

import aoc23.Day06Domain.DesertIsland
import aoc23.Day06Parser.toDesertIsland
import aoc23.Day06Solution.part1Day06
import aoc23.Day06Solution.part2Day06
import common.Collections.product
import common.Year23

object Day06 : Year23 {
    fun List<String>.part1(): Long = part1Day06()

    fun List<String>.part2(): Long = part2Day06()
}

object Day06Solution {
    fun List<String>.part1Day06(): Long =
        with(toDesertIsland()) {
            records.map(::numberOfWaysToBeat)
        }.product()


    fun List<String>.part2Day06(): Long =
        with(toDesertIsland()) {
            numberOfWaysToBeat(record)
        }
}

object Day06Domain {
    data class RaceRecord(val time: Long, val distance: Long)

    data class Race(val buttonTime: Long, val duration: Long) {
        fun distance() = (duration - buttonTime) * buttonTime
    }

    data class DesertIsland(
        val records: List<RaceRecord>
    ) {
        val record: RaceRecord =
            RaceRecord(
                time = records.joinToString("") { it.time.toString() }.toLong(),
                distance = records.joinToString("") { it.distance.toString() }.toLong()
            )

        fun numberOfWaysToBeat(record: RaceRecord): Long =
            (0..record.time).toList()
                .filter { Race(it, record.time).distance() > record.distance }
                .size
                .toLong()
    }
}

object Day06Parser {
    fun List<String>.toDesertIsland(): DesertIsland {
        fun String.toLongs(): List<Long> =
            this
                .split(":").last().split(" ")
                .filterNot { it.isBlank() }.map { it.toLong() }

        val times = this[0].toLongs()
        val distances = this[1].toLongs()
        return DesertIsland(
            records = times.zip(distances).map { Day06Domain.RaceRecord(it.first, it.second.toLong()) }
        )
    }
}
