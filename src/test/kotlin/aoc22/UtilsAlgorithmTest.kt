package aoc22

import aoc22.Algorithm.Node
import aoc22.Algorithm.Cost
import aoc22.Algorithm.Dijkstra
import aoc22.Algorithm.HasCost
import aoc22.Algorithm.HasNode
import aoc22.UtilsAlgorithmTest.MateyGrid.middle
import aoc22.UtilsAlgorithmTest.MateyGrid.middleLonger
import aoc22.UtilsAlgorithmTest.MateyGrid.end
import aoc22.UtilsAlgorithmTest.MateyGrid.noNeighbours
import aoc22.UtilsAlgorithmTest.MateyGrid.start
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UtilsAlgorithmTest {

    data class Matey(val name: String)

    data class MateyNode(
        override val value: Matey,
        override val neighbours: List<Cost<Matey>>,
    ) : Node<Matey>

    class MateyDijkstra(
        start: Node<Matey>
    ) : Dijkstra<Matey>(start) {
        override val isEnd: (HasNode<Matey>) -> Boolean = { it.node.value.name == "end" }
        override fun neighbours(node: HasNode<Matey>): List<HasCost<Matey>> = node.node.neighbours
    }

    object MateyGrid {
        private val endBeyond =
            MateyNode(
                Matey("endBeyond"),
                listOf()
            )
        val end =
            MateyNode(
                Matey("end"),
                listOf(Cost(endBeyond, 1))
            )
        val middleLonger =
            MateyNode(
                Matey("middleLonger"),
                listOf(Cost(end, 1))
            )
        val middle =
            MateyNode(
                Matey("middle"),
                listOf(Cost(middleLonger, 1), Cost(end, 1))
            )
        val noNeighbours =
            MateyNode(
                Matey("noNeighbours"),
                listOf()
            )
        val start =
            MateyNode(
                Matey("start"),
                listOf(Cost(middle, 2), Cost(noNeighbours, 1)))

    }

    @Test
    fun `test Dijkstra shortest distances`() {
        assertEquals(
            mapOf<Node<Matey>, Int>(
                start to 0,
                middle to 2,
                middleLonger to 3,
                end to 3,
                noNeighbours to 1
            ),
            MateyDijkstra(start).shortestDistances()
        )
    }

    @Test
    fun `test Dijkstra shortest path`() {
        assertEquals(
            listOf(start, middle, end),
            MateyDijkstra(start).shortestPath()
        )
    }
}