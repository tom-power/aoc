package common.graph

import java.util.*

data class Edge<T, NodeT : Node<T>>(
    val node: NodeT,
    val cost: Int,
)

interface Node<T> {
    val value: T
}

abstract class Dijkstra<T, NodeT : Node<T>>(
    private val monitoring: Boolean = false
) {
    abstract val start: () -> NodeT
    abstract val isEnd: (NodeT) -> Boolean
    abstract fun next(node: NodeT): List<Edge<T, NodeT>>

    fun shortestPath(): Int = FindShortestPath().invoke()

    val shortestPaths = mutableListOf<Pair<List<Node<T>>, Int>>()

    inner class FindShortestPath {
        private val seen = mutableSetOf<NodeT>()
        private val queue = PriorityQueue<State<T, NodeT>>()

        fun invoke(): Int {
            queue.add(State(Edge(start(), 0), 0, listOf()))

            while (queue.isNotEmpty()) {
                val (current, acc, list) = queue.poll()

                if (isEnd(current.node)) {
                    return (acc + current.cost).also {
                        if (monitoring) {
                            shortestPaths.add(Pair(list + current.node, acc + current.cost))
                        }
                    }
                }

                val nextSteps =
                    next(current.node)
                        .filter { next -> seen.add(next.node) }
                        .map { next ->
                            State(
                                edge = next,
                                cost = acc + current.cost,
                                path = if (monitoring) list + current.node else listOf()
                            )
                        }

                queue.addAll(nextSteps)
            }

            error("end not found")
        }
    }

    data class State<T, NodeT : Node<T>>(
        val edge: Edge<T, NodeT>,
        val cost: Int,
        val path: List<Node<T>>
    ) : Comparable<State<T, NodeT>> {
        override fun compareTo(other: State<T, NodeT>): Int =
            value().compareTo(other.value())

        fun value(): Int = edge.cost + cost
    }
}
