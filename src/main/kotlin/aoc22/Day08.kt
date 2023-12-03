package aoc22

import common.Collections.product
import aoc22.Day08Solution.visibleTreeCount
import aoc22.Day08Solution.maxScenicScore
import common.Space2D.Point
import common.Space2D.Parser.toPointToChar
import common.Year22

object Day08: Year22() {
    fun List<String>.part1(): Int = visibleTreeCount()

    fun List<String>.part2(): Int = maxScenicScore()
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
            else                        -> tallerTreesInView.minOf { tree.point.distanceTo(it.point) }
        }
    }

    private fun List<String>.toTrees(): List<Tree> =
        toPointToChar()
            .map { (p, c) -> Tree(p, c.digitToInt()) }

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

