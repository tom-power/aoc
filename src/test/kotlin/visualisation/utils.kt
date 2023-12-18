package visualisation

import common.Space2D
import common.Space2D.toMaxPoints
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