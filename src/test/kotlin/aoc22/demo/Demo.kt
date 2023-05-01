package aoc22.demo

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        launch { day09Visualisation() }
        launch { day12Visualisation() }
        launch { day14Visualisation() }
        launch { day17Visualisation() }
        launch { day22Visualisation() }
        launch { day23Visualisation() }
    }
}

