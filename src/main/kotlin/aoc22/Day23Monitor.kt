package aoc22

import aoc22.Space2D.Point
import aoc22.Space2D.print

class ElfMonitor(
    private val frame: Set<Point> = setOf()
) : Monitor<Elves> {
    private val elvesList: MutableList<Elves> = mutableListOf()

    override fun invoke(elves: Set<Elf>) {
        elvesList.add(elves)
    }

    override fun print(): List<String> =
        elvesList.map { elves ->
            (elves.map { it } + frame).print()
        }

}