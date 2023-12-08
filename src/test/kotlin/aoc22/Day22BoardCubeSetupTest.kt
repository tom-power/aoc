package aoc22

import aoc22.Day22DomainWrap.CubeWrap
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import aoc22.Day22DomainWrap.withCubeWrap
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22BoardCubeSetupTest {

    private fun String.toPath() = listOf("", this).toPath()

    private val exampleCubeBoard = Day22.readInputExample().toBoard().withCubeWrap()

    private fun CubeWrap.mappingsFor(point: Point): Set<EdgePointPair> =
        edgePointPairs
            .filter { edgePointPair ->
                edgePointPair.run { listOf(first.point, second.point).any { it == point } }
            }.toSet()

    @Test
    fun `can make CubeWrap mappings`() {
        val cubeWrap = exampleCubeBoard.wrap as CubeWrap
        assertEquals(25, cubeWrap.edgePointPairs.size)
    }

    @Test
    fun `can make CubeWrap mappings normal`() {
        val cubeWrap = exampleCubeBoard.wrap as CubeWrap

        val normalListOf = listOf(
            Pair(
                EdgePoint(point = Point(x = 5, y = -4), directionOnEnter = Down),
                EdgePoint(point = Point(x = 8, y = -1), directionOnEnter = Right)
            ),
        )
        assertEquals(normalListOf.toSet(), cubeWrap.mappingsFor(Point(8, -1)))
    }

    @Test
    fun `can make CubeWrap mappings outside corner`() {
        val cubeWrap = exampleCubeBoard.wrap as CubeWrap

        val outsideCornerListOf = listOf(
            Pair(
                EdgePoint(point = Point(x = 4, y = -4), directionOnEnter = Down),
                EdgePoint(point = Point(x = 8, y = 0), directionOnEnter = Right)
            ),
            Pair(
                EdgePoint(point = Point(x = 3, y = -4), directionOnEnter = Down),
                EdgePoint(point = Point(x = 8, y = 0), directionOnEnter = Down)
            )
        )

        assertEquals(outsideCornerListOf.toSet(), cubeWrap.mappingsFor(Point(8, 0)))
    }
}