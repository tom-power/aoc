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
        toDesertIsland()
            .numberOfWaysToBeatRecords()

    fun List<String>.part2Day06(): Long =
        toDesertIsland()
            .numberOfWaysToBeatRecord()
}

object Day06Domain {
    data class RaceRecord(val time: Long, val distance: Long)
    data class Race(val buttonTime: Long, val duration: Long)
    data class DesertIsland(
        val records: List<RaceRecord>
    ) {
        private val record: RaceRecord =
            RaceRecord(
                time = records.joinToString("") { it.time.toString() }.toLong(),
                distance = records.joinToString("") { it.distance.toString() }.toLong()
            )

        fun numberOfWaysToBeatRecords(): Long = records.map { numberOfWaysToBeat(it) }.product()
        fun numberOfWaysToBeatRecord(): Long = numberOfWaysToBeat(record)

        private fun numberOfWaysToBeat(record: RaceRecord): Long =
            (0..record.time).toList()
                .filter { buttonTime -> distanceFor(buttonTime, record.time) > record.distance }
                .size
                .toLong()

        private fun distanceFor(buttonTime: Long, duration: Long): Long = (duration - buttonTime) * buttonTime
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
