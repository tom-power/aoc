package aoc23

import common.Year23
import aoc23.Day15Domain.LensLibrary
import aoc23.Day15Parser.toLensLibrary

object Day15 : Year23 {
    fun List<String>.part1(): Int =
        toLensLibrary()
            .sumOfSteps()

    fun List<String>.part2(): Int =
        toLensLibrary()
            .apply { updateBoxes() }
            .totalFocusingPower()
}

object Day15Domain {
    data class LensLibrary(
        private val steps: List<String>
    ) {
        fun sumOfSteps(): Int = steps.sumOf { it.hash() }

        private val boxes = MutableList<MutableList<Lens>>(256) { mutableListOf() }

        fun totalFocusingPower(): Int = boxes.focusingPower()

        fun updateBoxes() {
            steps.forEach { step ->
                val label = step.filter { it.isLetter() }
                val index = label.hash()
                val box = boxes[index]

                when {
                    step.contains("-") -> {
                        boxes[index] =
                            box
                                .filterNot { lens -> lens.label == label }
                                .toMutableList()
                    }

                    step.contains("=") -> {
                        val newLens =
                            Lens(
                                label = label,
                                focalLength = step.filter { it.isDigit() }.toInt()
                            )

                        val indexOfLens = box.indexOfFirst { it.label == newLens.label }.takeIf { it != -1 }

                        boxes[index] =
                            box.apply {
                                indexOfLens
                                    ?.let { this[indexOfLens] = newLens }
                                    ?: this.add(newLens)
                            }
                    }
                }
            }
        }
    }

    private fun String.hash(): Int =
        this
            .map { it.code }
            .fold(0) { acc, i ->
                (acc + i) * 17 % 256
            }

    private fun List<MutableList<Lens>>.focusingPower(): Int =
        this.mapIndexed { index, box ->
            box.mapIndexed { slot, lens ->
                (index + 1) * (slot + 1) * lens.focalLength
            }.sum()
        }.sum()

    data class Lens(
        val label: String,
        val focalLength: Int
    )
}

object Day15Parser {
    fun List<String>.toLensLibrary(): LensLibrary =
        LensLibrary(
            steps = this.first().split(",")
        )
}
