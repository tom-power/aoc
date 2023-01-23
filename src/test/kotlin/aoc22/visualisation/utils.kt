package aoc22.visualisation

import com.varabyte.kotter.foundation.anim.text
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.session
import java.time.Duration

fun List<String>.animate() {
    session {
        val framesAnim =
            textAnimOf(
                frames = this@animate,
                frameDuration = Duration.ofMillis(500),
            )

        section {
            text(framesAnim)
        }.run { waitForSignal() }
    }
}

fun List<String>.freezeAt(round: Int): List<String> = listOf(this[round-1])