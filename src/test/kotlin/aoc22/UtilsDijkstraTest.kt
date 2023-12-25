package aoc22

import common.graph.Dijkstra
import common.graph.Node
import common.graph.Edge
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UtilsDijkstraTest {
    data class Matey(
        val name: String
    )

    private val endBeyond = Matey("endBeyond")
    private val end = Matey("end")
    private val middleExtra = Matey("middleExtra")
    private val middleBig = Matey("middleBig")
    private val middle = Matey("middle")
    private val start = Matey("start")
    private val deadEnd = Matey("deadEnd")

    private val mateyStepMap: Map<Matey, List<Edge<Matey, MateyNode>>> =
        mapOf(
            endBeyond to listOf(),
            end to
                listOf(
                    Edge(MateyNode(endBeyond), 10)
                ),
            middleExtra to
                listOf(
                    Edge(MateyNode(end), 10)
                ),
            middleBig to
                listOf(
                    Edge(MateyNode(end), 10)
                ),
            middle to
                listOf(
                    Edge(MateyNode(middleExtra), 10),
                    Edge(MateyNode(end), 13),
                    Edge(MateyNode(start), 10)
                ),
            deadEnd to listOf(),
            start to listOf(
                Edge(MateyNode(middleBig), 40),
                Edge(MateyNode(middle), 22),
                Edge(MateyNode(deadEnd), 10),
            ),
        )

    val seen = mutableListOf<Matey>()

    data class MateyNode(
        override val value: Matey,
    ) : Node<Matey>

    inner class MateyDijkstra : Dijkstra<Matey, MateyNode>() {
        override val start: () -> MateyNode = { MateyNode(this@UtilsDijkstraTest.start) }
        override val isEnd: (MateyNode) -> Boolean = { it.value.name == "end" }
        override fun next(node: MateyNode): List<Edge<Matey, MateyNode>> = mateyStepMap[node.value]!!
    }

    @Test
    fun `test Dijkstra lowest cost`() {
        assertEquals(
            35,
            MateyDijkstra().shortestPath()
        )
    }
}