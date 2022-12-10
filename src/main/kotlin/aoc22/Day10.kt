package aoc22

import aoc22.Day10Solution.toCycles
import aoc22.Day10Solution.toInstructions
import aoc22.Day10Solution.toRegisters
import aoc22.Day10SolutionPart1.part1Day10
import aoc22.Day10SolutionPart2.part2Day10

object Day10 : Day<String, Int, String> {
    override fun List<String>.part1(): Int = part1Day10()

    override fun List<String>.part2(): String = part2Day10()
}

object Day10SolutionPart1 {
    fun List<String>.part1Day10(): Int =
        toInstructions()
            .toRegisters()
            .toCycles()
            .toSamples()
            .toSignalStrength()
            .sumOf { it }

    private fun List<Day10Solution.Cycle>.toSamples(): List<Day10Solution.Cycle> =
        drop(19).windowed(1, 40).map { it.first() }

    private fun List<Day10Solution.Cycle>.toSignalStrength(): List<Int> = map { it.value * it.registerDuring.x }
}

object Day10SolutionPart2 {
    fun List<String>.part2Day10(): String =
        toInstructions()
            .toRegisters()
            .toCycles()
            .draw()

    private const val width = 40

    private fun List<Day10Solution.Cycle>.draw(): String =
        chunked(width)
            .mapIndexed { index, cycles ->
                cycles.map { cycle ->
                    when {
                        cycle.toPixel(index) in cycle.toSprite() -> "#"
                        else                                     -> "."
                    }
                }.joinToString("")
            }.joinToString(System.lineSeparator())

    private fun Day10Solution.Cycle.toPixel(index: Int): Int = (value - (index * width)) - 1

    private fun Day10Solution.Cycle.toSprite(): IntRange = registerDuring.x.run { this - 1..this + 1 }
}

object Day10Solution {
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
                Noop -> acc + last
                is Addx -> acc + last + Register(x = last.x + instruction.v)
            }
        }

    fun List<Register>.toCycles(): List<Cycle> =
        mapIndexed { index, register ->
            Cycle(
                value = index + 1,
                registerDuring = getOrElse(index - 1) { register },
                registerAfter = register
            )
        }

    sealed class Instruction
    object Noop : Instruction()
    data class Addx(val v: Int) : Instruction()

    data class Register(val x: Int)
    data class Cycle(val value: Int, val registerDuring: Register, val registerAfter: Register)
}

