package template

import template.Day00Solution.part1Day00
import template.Day00Solution.part2Day00
import common.Misc.log
import common.Year00

object Day00 : Year00() {
    fun List<String>.part1(): Int = part1Day00()

    fun List<String>.part2(): Int = part2Day00()
}

object Day00Solution {
    fun List<String>.part1Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }

    fun List<String>.part2Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

object Day00Runner {

}

private typealias Day00Type = Any

object Day00Domain {

}

object Day00Parser {
    fun List<String>.toDay00(): List<Day00Type> = TODO()
}
