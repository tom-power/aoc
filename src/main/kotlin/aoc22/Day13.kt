@file:Suppress("UNCHECKED_CAST")

package aoc22

import aoc22.Day13Domain.compareTo
import aoc22.Day13Domain.packetComparator
import aoc22.Day13Parser.toPacket
import aoc22.Day13Parser.toPackets
import aoc22.Day13Runner.productOfSortedIndexesOf
import aoc22.Day13Runner.toIsPairsInOrder
import aoc22.Day13Runner.sumOfIndicesOfTruthy
import aoc22.Day13Solution.part1Day13
import aoc22.Day13Solution.part2Day13
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import common.Year22

object Day13: Year22() {
    fun List<String>.part1(): Int = part1Day13()

    fun List<String>.part2(): Int = part2Day13()
}

object Day13Solution {
    fun List<String>.part1Day13(): Int =
        toPackets()
            .toIsPairsInOrder()
            .sumOfIndicesOfTruthy()

    fun List<String>.part2Day13(): Int =
        toPackets()
            .productOfSortedIndexesOf(
                divider1 = "[[2]]".toPacket(),
                divider2 = "[[6]]".toPacket()
            )
}

object Day13Runner {
    fun List<Packet>.toIsPairsInOrder(): List<Boolean> =
        chunked(2)
            .map { it.first().compareTo(it.last()) < 0 }

    fun List<Boolean>.sumOfIndicesOfTruthy(): Int =
        mapIndexedNotNull { index, b -> (index + 1).takeIf { b } }
            .sum()

    fun List<Packet>.productOfSortedIndexesOf(
        divider1: Packet,
        divider2: Packet
    ): Int =
        (this + listOf(divider1) + listOf(divider2))
            .sortedWith(packetComparator)
            .let { (it.indexOf(divider1) + 1) * (it.indexOf(divider2) + 1) }
}

private typealias Packet = List<Any>

object Day13Domain {
    val packetComparator = Comparator<Packet> { o1, o2 -> o1.compareTo(o2) }

    fun Any.compareTo(other: Any): Int {
        when {
            this is List<*> && other is List<*> -> {
                if (this.isEmpty() || other.isEmpty()) return this.size.compareTo(other.size)

                return this.first()!!.compareTo(other.first()!!)
                    .takeIf { it != 0 }
                    ?: this.drop(1).compareTo(other.drop(1))
            }
            this is Int && other is Int -> return this.compareTo(other)
            else -> return this.toPacket().compareTo(other.toPacket())
        }
    }

    private fun Any.toPacket(): Packet =
        when (this) {
            is Int -> listOf(this)
            is List<*> -> this as List<Any>
            else -> error("not a packet")
        }
}

object Day13Parser {
    private val mapper = ObjectMapper().registerKotlinModule()

    fun List<String>.toPackets(): List<Packet> =
        filterNot { it.isEmpty() }
            .map { it.toPacket() }

    fun String.toPacket(): Packet = mapper.readValue(this)
}