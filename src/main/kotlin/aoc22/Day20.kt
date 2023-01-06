package aoc22

import aoc22.Day20Domain.Mixer
import aoc22.Day20Solution.part1Day20
import aoc22.Day20Solution.part2Day20
import aoc22.Day20Parser.toMixer
import aoc22.Day20Runner.coordinates
import aoc22.Day20Runner.decrypt

object Day20 : Day {
    fun List<String>.part1(): Long = part1Day20()

    fun List<String>.part2(): Long = part2Day20()
}

object Day20Solution {
    fun List<String>.part1Day20(): Long =
        toMixer(key = 1L)
            .decrypt(times = 1)
            .coordinates()

    fun List<String>.part2Day20(): Long =
        toMixer(key = 811589153L)
            .decrypt(times = 10)
            .coordinates()
}

object Day20Runner {
    fun List<Mixer>.decrypt(times: Int, mixers: MutableList<Mixer> = this.toMutableList()): List<Mixer> =
        when (times) {
            0 -> mixers
            else -> decrypt(times - 1, this.decryptWith(mixers).toMutableList())
        }

    private fun List<Mixer>.decryptWith(acc: MutableList<Mixer>): List<Mixer> =
        foldIndexed(acc) { originalIndex, acc, mixer ->
            val index = acc.indexOfFirst { it.originalIndex == originalIndex }
            acc.apply {
                removeAt(index)
                add((index + mixer.value).mod(acc.size), mixer)
            }
        }

    fun List<Mixer>.coordinates(): Long =
        indexOfFirst { it.value == 0L }.let { zero ->
            listOf(1000, 2000, 3000).sumOf { this[(zero + it).mod(size)].value }
        }
}

object Day20Domain {
    data class Mixer(val originalIndex: Int, val value: Long)
}

object Day20Parser {
    fun List<String>.toMixer(key: Long): List<Mixer> =
        mapIndexed { index, s ->
            Mixer(
                originalIndex = index,
                value = s.toLong() * key
            )
        }
}
