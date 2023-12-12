package aoc22.demo

import aoc22.Day17
import aoc22.Day17Domain
import aoc22.Day17Parser.toJets
import aoc22.Day17Runner
import common.Monitoring
import aoc22.visualisation.animate
import common.Input.readInputExample

fun day17Visualisation() {
    framesFor(input = Day17.readInputExample())
        .animate()
}

private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointMonitor()
        .apply {
            Day17Runner.DropWithSimulation(
                chamber = Day17Domain.Chamber(jets = input.toJets(), monitor = this),
                maxDrops = 10,
            ).invoke()
        }.toLoggable()