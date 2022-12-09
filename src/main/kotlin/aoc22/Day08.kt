package aoc22

import aoc22.Collections.product
import aoc22.Day08Solution.visibleTreeCount
import aoc22.Day08Solution.maxScenicScore
import aoc22.Matrix.Point
import aoc22.Matrix.distanceBetween

object Day08 : Day<String, Int> {
    override fun List<String>.part1(): Int = visibleTreeCount()

    override fun List<String>.part2(): Int = maxScenicScore()
}

object Day08Solution {
    fun List<String>.visibleTreeCount(): Int =
        toTrees().run {
            count { tree ->
                viewsFor(tree).any { it.edgeIsVisibleFrom(tree) }
            }
        }

    private fun List<Tree>.edgeIsVisibleFrom(tree: Tree): Boolean = isEmpty() || all { it.height < tree.height }

    fun List<String>.maxScenicScore(): Int =
        toTrees().run {
            maxOf { tree ->
                viewsFor(tree).map { it.viewingDistanceFor(tree) }.product()
            }
        }

    private fun List<Tree>.viewingDistanceFor(tree: Tree): Int {
        val tallerTreesInView = filter { it.height >= tree.height }
        return when {
            isEmpty()                   -> 0
            tallerTreesInView.isEmpty() -> size
            else                        -> tallerTreesInView.minOf { distanceBetween(tree.point, it.point) }
        }
    }

    private fun List<String>.toTrees(): List<Tree> =
        mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Tree(Point(x, y), c.digitToInt())
            }
        }.flatten()

    private data class Tree(
        val point: Point,
        val height: Int,
    )

    private fun List<Tree>.viewsFor(tree: Tree): List<List<Tree>> {
        fun Tree.x(): Int = this.point.x
        fun Tree.y(): Int = this.point.y
        return listOf(
            filter { it.x() == tree.x() && it.y() > tree.y() },
            filter { it.x() == tree.x() && it.y() < tree.y() },
            filter { it.y() == tree.y() && it.x() > tree.x() },
            filter { it.y() == tree.y() && it.x() < tree.x() },
        )
    }
}

