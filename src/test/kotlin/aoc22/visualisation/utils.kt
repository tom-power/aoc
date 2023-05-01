package aoc22.visualisation

import aoc22.Space2D
import aoc22.Space2D.toMaxPoints
import com.varabyte.kotter.foundation.anim.text
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.session
import java.time.Duration

internal fun List<String>.animate(frameDuration: Long = 100) {
    session {
        val framesAnim =
            textAnimOf(
                frames = this@animate,
                frameDuration = Duration.ofMillis(frameDuration),
            )

        section {
            text(framesAnim)
        }.run { waitForSignal() }
    }
}

internal fun List<String>.freezeAt(round: Int): List<String> = listOf(this[round-1])

internal fun Collection<Space2D.Point>.toFrame(): Collection<Space2D.Point> {
    val minX = this.minOfOrNull { it.x }
    val maxX = this.maxOfOrNull { it.x }
    val minY = this.minOfOrNull { it.y }
    val maxY = this.maxOfOrNull { it.y }
    return toMaxPoints().filter { it.x == minX || it.x == maxX || it.y == minY || it.y == maxY}
}