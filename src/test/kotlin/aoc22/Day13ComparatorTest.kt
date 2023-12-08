package aoc22

import aoc22.Day13ComparatorInput.intsLarger
import aoc22.Day13ComparatorInput.intsSmaller
import aoc22.Day13ComparatorInput.mixedLarger
import aoc22.Day13ComparatorInput.mixedLarger2
import aoc22.Day13ComparatorInput.mixedSmaller
import aoc22.Day13ComparatorInput.mixedSmaller2
import aoc22.Day13Domain.compareTo
import aoc22.Day13Parser.toPacket
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day13ComparatorTest {

    @Nested
    inner class CompareTo {
        @Nested
        inner class CompareAllInts {
            @Test
            fun `compare all ints gt`() {
                assertEquals(-1, intsSmaller.compareTo(intsLarger))
            }
            @Test
            fun `compare all ints same`() {
                assertEquals(0, intsSmaller.compareTo(intsSmaller))
            }

            @Test
            fun `compare all ints lt`() {
                assertEquals(1, intsLarger.compareTo(intsSmaller))
            }
        }

        @Nested
        inner class CompareMixed {
            @Test
            fun `compare mixed lt`() {
                assertEquals(-1, mixedSmaller.compareTo(mixedLarger))
            }
            @Test
            fun `compare mixed gt`() {
                assertEquals(1, mixedLarger.compareTo(mixedSmaller))
            }
            @Test
            fun `compare mixed same`() {
                assertEquals(0, mixedSmaller.compareTo(mixedSmaller))
            }
        }

        @Nested
        inner class CompareMixed2 {
            @Test
            fun `compare mixed lt`() {
                assertEquals(-1, mixedSmaller2.compareTo(mixedLarger2))
            }
            @Test
            fun `compare mixed gt`() {
                assertEquals(1, mixedLarger2.compareTo(mixedSmaller2))
            }
            @Test
            fun `compare mixed same`() {
                assertEquals(0, mixedSmaller2.compareTo(mixedSmaller2))
            }
        }
    }
}

object Day13ComparatorInput {
    val intsSmaller = "[1, 1, 1, 6]".toPacket()
    val intsLarger = "[1, 1, 1, 7]".toPacket()

    val mixedSmaller = "[[8, [[7, 10, 10, 5], [8, 4, 9]], 3, 5], [[[3, 9, 4], 5, [7, 5, 5]], [[3, 2, 5], [10], [5, 5], 0, [8]]], [4, 2, [], [[7, 5, 6, 3, 0], [4, 4, 10, 7], 6, [8, 10, 9]]], [[4, [], 4], 10, 1]]".toPacket()
    val mixedLarger = "[[[[8], [3, 10], [7, 6, 3, 7, 4], 1, 8]]]".toPacket()

    val mixedSmaller2 = "[[[], 2, 3, 5]]".toPacket()
    val mixedLarger2 = "[[[], 10], [], [3, [2]], [[6], 8, [[8, 7, 4, 9]], [[4, 3, 10], [10, 6, 9], [4, 8, 10, 2, 5], [5, 6, 9, 3], 3]], [[[0, 8], [], 8, 4, 10], [0, 1], 5, [10, [0, 7, 7, 3, 3], [8, 7], 8]]]".toPacket()
}