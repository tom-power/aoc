package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import aoc22.Day22DomainWrap.withCubeWrap
import common.Space2D.Direction.*
import common.Space2D.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22BoardCubeTest {

    private fun String.toPath() = listOf("", this).toPath()

    private val exampleCubeBoard = Day22.readInputExample().toBoard().withCubeWrap()

    @Test
    fun `can wrap`() {
        assertEquals(State(Point(x = 4, y = -4), Down), exampleCubeBoard.follow("RR1".toPath()))
//        exampleCubeBoard.monitor.print()
//                <..#
//                .#..
//                #...
//                ....
//        ...#v......#
//        ........#...
//        ..#....#....
//        ..........#.
//                ...#....
//                .....#..
//                .#......
//                ......#.
    }

    @Test
    fun `can wrap 2`() {
        assertEquals(State(Point(x = 14, y = -10), Down), exampleCubeBoard.follow("10R5L5".toPath()))
//        exampleCubeBoard.monitor.print()
//                ..v#
//                .#..
//                #...
//                ....
//        ...#.......#
//        ........#.>.
//        ..#....#....
//        ..........#.
//                ...#....
//                .....#..
//                .#....v.
//                ......#.
    }
}