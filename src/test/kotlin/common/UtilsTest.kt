package common

import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.lastIn
import common.Space2D.nextIn
import common.Space2D.topLeft
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UtilsTest {
    @Nested
    inner class CollectionPoint {
        private val tiles =
            listOf(
                ".8u",
                ".|/",
                "...",
                "#.-"
            ).toPointToChars().toMap()

        private val tilesTopLeft = tiles.keys.topLeft
        private val pointsThatDivert = tiles.filterNot { it.value == '.' }.keys

        @Test
        fun `nextIn works`() {
            with(pointsThatDivert) {
                assertEquals(
                    '#',
                    tiles[tilesTopLeft.nextIn(South)]
                )
                assertEquals(
                    '-',
                    tiles[tilesTopLeft.nextIn(South)?.nextIn(East)]
                )
                assertEquals(
                    '/',
                    tiles[tilesTopLeft.nextIn(South)?.nextIn(East)?.nextIn(North)]
                )
                assertEquals(
                    '|',
                    tiles[tilesTopLeft.nextIn(South)?.nextIn(East)?.nextIn(North)?.nextIn(West)]
                )
                assertEquals(
                    null,
                    tiles[tilesTopLeft.nextIn(South)?.nextIn(East)?.nextIn(North)?.nextIn(West)?.nextIn(West)]
                )
            }
        }

        @Test
        fun `lastIn works`() {
            with(pointsThatDivert) {
                assertEquals(
                    '#',
                    tiles[tilesTopLeft.lastIn(South)]
                )
                assertEquals(
                    '-',
                    tiles[tilesTopLeft.lastIn(South)?.lastIn(East)]
                )
                assertEquals(
                    'u',
                    tiles[tilesTopLeft.lastIn(South)?.lastIn(East)?.lastIn(North)]
                )
                assertEquals(
                    '.',
                    tiles[tilesTopLeft.lastIn(South)?.lastIn(East)?.lastIn(North)?.lastIn(West)]
                )
            }
        }
    }
}