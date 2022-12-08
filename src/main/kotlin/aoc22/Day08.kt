package aoc22

import aoc22.Day08Solution.visibleTreeCount
import aoc22.Day08Solution.maxScenicScore
import kotlin.math.abs

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
                viewsFor(tree).map { it.viewingDistanceFor(tree) }.scenicScore()
            }
        }

    private fun List<Int>.scenicScore(): Int = reduce { acc, viewingDistance -> acc * viewingDistance }

    private fun List<Tree>.viewingDistanceFor(tree: Tree): Int {
        val tallerTreesInView = filter { it.height >= tree.height }
        return when {
            isEmpty()                   -> 0
            tallerTreesInView.isEmpty() -> size
            else                        -> tallerTreesInView.minOf { distanceBetween(tree, it) }
        }
    }

    private fun distanceBetween(tree: Tree, other: Tree): Int =
        listOf(
            tree.x - other.x,
            tree.y - other.y,
        ).map { abs(it) }.max()

    private fun List<String>.toTrees(): List<Tree> =
        mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Tree(x, y, c.digitToInt())
            }
        }.flatten()

    private data class Tree(
        val x: Int,
        val y: Int,
        val height: Int,
    )

    private fun List<Tree>.viewsFor(tree: Tree): List<List<Tree>> =
        listOf(
            filter { it.x == tree.x && it.y > tree.y },
            filter { it.x == tree.x && it.y < tree.y },
            filter { it.y == tree.y && it.x > tree.x },
            filter { it.y == tree.y && it.x < tree.x },
        )
}

