package aoc22

import aoc22.Day10Domain.Addx
import aoc22.Day10Domain.Cycle
import aoc22.Day10Domain.Instruction
import aoc22.Day10Domain.Noop
import aoc22.Day10Domain.Register
import aoc22.Day10Parser.toInstructions
import aoc22.Day10Runner.run
import aoc22.Day10SolutionPart1.part1Day10
import aoc22.Day10SolutionPart2.part2Day10
import common.Year22

object Day10: Year22 {
    fun List<String>.part1(): Int = part1Day10()

    fun List<String>.part2(): String = part2Day10()
}

object Day10SolutionPart1 {
    fun List<String>.part1Day10(): Int =
        toInstructions()
            .run()
            .toSamples()
            .toSignalStrength()
            .sumOf { it }

    private fun List<Cycle>.toSamples(): List<Cycle> =
        drop(19).windowed(1, 40).map { it.first() }

    private fun List<Cycle>.toSignalStrength(): List<Int> = map { it.value * it.registerDuring.x }
}

object Day10SolutionPart2 {
    fun List<String>.part2Day10(): String =
        toInstructions()
            .run()
            .draw()

    private const val width = 40

    private fun List<Cycle>.draw(): String =
        chunked(width)
            .mapIndexed { index, cycles ->
                cycles.map { cycle ->
                    when {
                        cycle.toPixel(index) in cycle.toSprite() -> "#"
                        else                                     -> "."
                    }
                }.joinToString("")
            }.joinToString(System.lineSeparator())

    private fun Cycle.toPixel(index: Int): Int = (value - (index * width)) - 1

    private fun Cycle.toSprite(): IntRange = registerDuring.x.run { this - 1..this + 1 }
}

object Day10Runner {
    fun List<Instruction>.run(): List<Cycle> = toRegisters().toCycles()

    private fun List<Instruction>.toRegisters(): List<Register> =
        fold(emptyList()) { acc, instruction ->
            val last = acc.lastOrNull() ?: Register(x = 1)
            when (instruction) {
                Noop    -> acc + last
                is Addx -> acc + last + Register(x = last.x + instruction.v)
            }
        }

    private fun List<Register>.toCycles(): List<Cycle> =
        mapIndexed { index, register ->
            Cycle(
                value = index + 1,
                registerDuring = getOrElse(index - 1) { register },
                registerAfter = register
            )
        }
}

object Day10Parser {
    fun List<String>.toInstructions(): List<Instruction> =
        map {
            when {
                it == "noop"          -> Noop
                it.startsWith("addx") -> Addx(it.split(" ")[1].toInt())
                else                  -> error("didn't find")
            }
        }
}

object Day10Domain {
    sealed class Instruction
    object Noop : Instruction()
    data class Addx(val v: Int) : Instruction()

    data class Register(val x: Int)
    data class Cycle(val value: Int, val registerDuring: Register, val registerAfter: Register)
}

