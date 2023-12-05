package template

import common.Misc.log
import common.Year00
import template.Day00Domain.Day00Type
import template.Day00Parser.toDay00
import template.Day00Solution.part1Day00
import template.Day00Solution.part2Day00

object Day00 : Year00() {
    fun List<String>.part1(): Int = part1Day00()

    fun List<String>.part2(): Int = part2Day00()
}

object Day00Solution {
    fun List<String>.part1Day00(): Int =
        toDay00().log()
            .let { 0 }

    fun List<String>.part2Day00(): Int =
        toDay00().log()
            .let { 0 }
}

object Day00Domain {
    data class Day00Type(val value: String)
}

object Day00Parser {
    fun List<String>.toDay00(): List<Day00Type> = TODO()
}
