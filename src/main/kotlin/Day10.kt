package aoc22

import aoc22.Day10Solution.part1Day10
import aoc22.Day10Solution.part2Day10
import aoc22.Misc.log

object Day10 : Day<String, Int, String> {
    override fun List<String>.part1(): Int = part1Day10()

    override fun List<String>.part2(): String = part2Day10()
}

object Day10Solution {

    fun List<String>.part1Day10(): Int =
        toInstructions()
            .toRegisters()//.also { it.mapIndexed { index, i -> index to i.x }.log() }
            .toCycles()//.also { it.filter { it.v in 0 .. 20 }.log() }
            .toSamples()//.log()
            .toSignalStrength()
            .sumOf { it }

    sealed class Instruction
    object Noop : Instruction()
    data class Addx(val v: Int) : Instruction()

    data class Register(val x: Int)
    data class Cycle(val v: Int, val registerDuring: Register, val registerAfter: Register)

    fun List<String>.toInstructions(): List<Instruction> =
        map {
            when {
                it == "noop"          -> Noop
                it.startsWith("addx") -> Addx(it.split(" ")[1].toInt())
                else                  -> error("didn't find")
            }
        }

    fun List<Instruction>.toRegisters(): List<Register> =
        fold(emptyList()) { acc, instruction ->
            val last = acc.lastOrNull() ?: Register(x = 1)
            when (instruction) {
                is Noop -> acc + last
                is Addx -> acc + last + Register(x = last.x + instruction.v)
                else    -> error("why?")
            }
        }

    fun List<Register>.toCycles(): List<Cycle> =
        mapIndexed { index, register ->
            Cycle(
                v = index + 1,
                registerDuring = getOrElse(index - 1) { register },
                registerAfter = register
            )
        }

    private fun List<Cycle>.toSignalStrength(): List<Int> = map { it.v * it.registerDuring.x }

    private fun List<Cycle>.toSamples(): List<Cycle> =
        drop(19).windowed(1, 40).map { it.first() }

    fun List<String>.part2Day10(): String =
        map { it.split(",") }.log()
            .let { "" }
}
