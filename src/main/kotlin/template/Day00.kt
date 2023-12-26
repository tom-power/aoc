package template

import common.Misc.log
import common.Year00
import template.Day00Domain.Day00Type
import template.Day00Parser.toDay00

object Day00 : Year00 {
    fun List<String>.part1(): Int =
        toDay00()
            .log()
            .let { 0 }

    fun List<String>.part2(): Int =
        toDay00()
            .log()
            .let { 0 }
}

object Day00Domain {
    class Day00Type(
        val value: String
    )
}

object Day00Parser {
    fun List<String>.toDay00(): Day00Type =
        TODO()
}
