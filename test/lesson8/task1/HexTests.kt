package lesson8.task1

import lesson8.task1.Direction.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class HexTests {

    @Test
    @Tag("Normal")
    fun hexPointDistance() {
        assertEquals(5, HexPoint(6, 1).distance(HexPoint(1, 4)))
        assertEquals(0, HexPoint(6, 1).distance(HexPoint(6, 1)))
    }

    @Test
    @Tag("Normal")
    fun hexagonDistance() {
        assertEquals(0, Hexagon(HexPoint(-999, -1000), 1).distance(Hexagon(HexPoint(-558, -999), 650)))
        assertEquals(2079, Hexagon(HexPoint(-558, -558), 136).distance(Hexagon(HexPoint(951, 632), 484)))
        assertEquals(2, Hexagon(HexPoint(1, 3), 1).distance(Hexagon(HexPoint(6, 2), 2)))
    }

    @Test
    @Tag("Trivial")
    fun hexagonContains() {
        assertTrue(Hexagon(HexPoint(3, 3), 1).contains(HexPoint(2, 3)))
        assertFalse(Hexagon(HexPoint(3, 3), 1).contains(HexPoint(4, 4)))
    }

    @Test
    @Tag("Easy")
    fun hexSegmentValid() {
        assertTrue(HexSegment(HexPoint(1, 3), HexPoint(5, 3)).isValid())
        assertFalse(HexSegment(HexPoint(-558, -1000), HexPoint(-558, -1000)).isValid())
        assertTrue(HexSegment(HexPoint(3, 1), HexPoint(3, 6)).isValid())
        assertTrue(HexSegment(HexPoint(1, 5), HexPoint(4, 2)).isValid())
        assertFalse(HexSegment(HexPoint(3, 1), HexPoint(6, 2)).isValid())
    }

    @Test
    @Tag("Normal")
    fun hexSegmentDirection() {
        assertEquals(RIGHT, HexSegment(HexPoint(1, 3), HexPoint(5, 3)).direction())
        assertEquals(UP_RIGHT, HexSegment(HexPoint(3, 1), HexPoint(3, 6)).direction())
        assertEquals(DOWN_RIGHT, HexSegment(HexPoint(1, 5), HexPoint(4, 2)).direction())
        assertEquals(LEFT, HexSegment(HexPoint(5, 3), HexPoint(1, 3)).direction())
        assertEquals(DOWN_LEFT, HexSegment(HexPoint(3, 6), HexPoint(3, 1)).direction())
        assertEquals(UP_LEFT, HexSegment(HexPoint(4, 2), HexPoint(1, 5)).direction())
        assertEquals(INCORRECT, HexSegment(HexPoint(3, 1), HexPoint(6, 2)).direction())
    }

    @Test
    @Tag("Easy")
    fun oppositeDirection() {
        assertEquals(LEFT, RIGHT.opposite())
        assertEquals(DOWN_LEFT, UP_RIGHT.opposite())
        assertEquals(UP_LEFT, DOWN_RIGHT.opposite())
        assertEquals(RIGHT, LEFT.opposite())
        assertEquals(DOWN_RIGHT, UP_LEFT.opposite())
        assertEquals(UP_RIGHT, DOWN_LEFT.opposite())
        assertEquals(INCORRECT, INCORRECT.opposite())
    }

    @Test
    @Tag("Normal")
    fun nextDirection() {
        assertEquals(UP_RIGHT, RIGHT.next())
        assertEquals(UP_LEFT, UP_RIGHT.next())
        assertEquals(RIGHT, DOWN_RIGHT.next())
        assertEquals(DOWN_LEFT, LEFT.next())
        assertEquals(LEFT, UP_LEFT.next())
        assertEquals(DOWN_RIGHT, DOWN_LEFT.next())
        assertThrows(IllegalArgumentException::class.java) {
            INCORRECT.next()
        }
    }

    @Test
    @Tag("Easy")
    fun isParallelDirection() {
        assertTrue(RIGHT.isParallel(RIGHT))
        assertTrue(RIGHT.isParallel(LEFT))
        assertFalse(RIGHT.isParallel(UP_LEFT))
        assertFalse(RIGHT.isParallel(INCORRECT))
        assertTrue(UP_RIGHT.isParallel(UP_RIGHT))
        assertTrue(UP_RIGHT.isParallel(DOWN_LEFT))
        assertFalse(UP_RIGHT.isParallel(UP_LEFT))
        assertFalse(INCORRECT.isParallel(INCORRECT))
        assertFalse(INCORRECT.isParallel(UP_LEFT))
    }

    @Test
    @Tag("Normal")
    fun hexPointMove() {
        assertEquals(HexPoint(3, 3), HexPoint(0, 3).move(RIGHT, 3))
        assertEquals(HexPoint(3, 5), HexPoint(5, 3).move(UP_LEFT, 2))
        assertEquals(HexPoint(5, 0), HexPoint(5, 4).move(DOWN_LEFT, 4))
        assertEquals(HexPoint(1, 1), HexPoint(1, 1).move(DOWN_RIGHT, 0))
        assertEquals(HexPoint(4, 2), HexPoint(2, 2).move(LEFT, -2))
        assertThrows(IllegalArgumentException::class.java) {
            HexPoint(0, 0).move(INCORRECT, 0)
        }
    }

    @Test
    @Tag("Hard")
    fun pathBetweenHexes() {
        assertEquals(
            1, pathBetweenHexes(HexPoint(y = -1000, x = -558), HexPoint(y = -1000, x = -558)).size
        )
        assertEquals(
            listOf(
                HexPoint(y = 2, x = 2),
                HexPoint(y = 3, x = 2),
                HexPoint(y = 4, x = 2),
                HexPoint(y = 4, x = 3),
                HexPoint(y = 5, x = 3)
            ), pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3))
        )
    }

    @Test
    @Tag("Impossible")
    fun hexagonByThreePoints() {
        assertEquals(
            221,
            hexagonByThreePoints(HexPoint(-377, -999), HexPoint(-558, -557), HexPoint(-557, -999))?.radius
        )
        assertEquals(
            443,
            hexagonByThreePoints(HexPoint(-1000, -999), HexPoint(-557, -558), HexPoint(-557, -558))?.radius
        )
        assertNull(hexagonByThreePoints(HexPoint(-557, -999), HexPoint(-965, -1000), HexPoint(448, 692)))
        assertEquals(
            3,
            hexagonByThreePoints(HexPoint(2, 3), HexPoint(3, 3), HexPoint(5, 3))?.radius
        )
        assertEquals(
            0,
            hexagonByThreePoints(HexPoint(2, 3), HexPoint(2, 3), HexPoint(2, 3))?.radius
        )
        assertEquals(
            Hexagon(HexPoint(4, 2), 2),
            hexagonByThreePoints(HexPoint(3, 1), HexPoint(2, 3), HexPoint(4, 4))
        )
        assertNull(
            hexagonByThreePoints(HexPoint(3, 1), HexPoint(2, 3), HexPoint(5, 4))
        )
    }

    @Test
    @Tag("Impossible")
    fun minContainingHexagon() {
        var points = arrayOf(HexPoint(3, 1), HexPoint(3, 2), HexPoint(5, 4), HexPoint(8, 1))
        var result = minContainingHexagon(*points)
        assertEquals(3, result.radius)
        assertTrue(points.all { result.contains(it) })
        points = arrayOf(
            HexPoint(-999, -999),
            HexPoint(-999, -73),
            HexPoint(-367, 799),
            HexPoint(-558, -132),
            HexPoint(-558, -1000),
            HexPoint(-557, -557),
            HexPoint(-1000, -603),
            HexPoint(-128, -558),
            HexPoint(-999, -558),
            HexPoint(-177, -557),
            HexPoint(-24, -1000),
            HexPoint(-999, -402),
            HexPoint(-999, -1000),
            HexPoint(-557, -1000),
            HexPoint(-558, -558),
            HexPoint(473, -999),
            HexPoint(-557, -1000),
            HexPoint(122, 395),
            HexPoint(403, -502),
            HexPoint(-558, -558),
            HexPoint(380, -999),
            HexPoint(-1000, -706),
            HexPoint(782, -886),
            HexPoint(-1000, -948),
            HexPoint(-1000, -999),
            HexPoint(-558, -557),
            HexPoint(459, -1000),
            HexPoint(-999, -557),
            HexPoint(-558, -656),
            HexPoint(-999, 791),
            HexPoint(-983, 603),
            HexPoint(-1000, -557),
            HexPoint(-999, -962),
            HexPoint(-558, -1000),
            HexPoint(-116, -1000),
            HexPoint(751, -4),
            HexPoint(-999, -977),
            HexPoint(-9, 467),
            HexPoint(-1000, -1000),
            HexPoint(903, -558),
            HexPoint(-82, -1000),
            HexPoint(-912, -999),
            HexPoint(-999, -558),
            HexPoint(-557, -557),
            HexPoint(-557, -892),
            HexPoint(-946, -557),
            HexPoint(-999, -558),
            HexPoint(-123, -558),
            HexPoint(-178, -1000),
            HexPoint(-558, -557),
            HexPoint(-1000, -1000),
            HexPoint(-1000, -557),
            HexPoint(-557, -557),
            HexPoint(676, -999),
            HexPoint(-863, -999),
            HexPoint(-1000, -557),
            HexPoint(204, 890),
            HexPoint(574, -330),
            HexPoint(-999, -999),
            HexPoint(314, 44),
            HexPoint(-558, -375),
            HexPoint(-1000, -558),
            HexPoint(-999, -261),
            HexPoint(-557, -557),
            HexPoint(733, -557),
            HexPoint(-936, -999),
            HexPoint(-558, 598),
            HexPoint(-558, -518),
            HexPoint(989, -557),
            HexPoint(-306, 143),
            HexPoint(-557, 123),
            HexPoint(-558, -296),
            HexPoint(-690, -342),
            HexPoint(-900, -557),
            HexPoint(969, 426),
            HexPoint(-557, -1000),
            HexPoint(-999, 1000),
            HexPoint(239, -367),
            HexPoint(-31, -558),
            HexPoint(-1000, 615),
            HexPoint(-1000, 802),
            HexPoint(-558, -999),
            HexPoint(-842, -557),
            HexPoint(-558, -745),
            HexPoint(-558, 134),
            HexPoint(-999, 973),
            HexPoint(-999, -557),
            HexPoint(-557, -974),
            HexPoint(-558, -999),
            HexPoint(-168, -999),
            HexPoint(-999, -999),
            HexPoint(289, -557),
            HexPoint(-610, -1000),
            HexPoint(-1000, 0),
            HexPoint(-999, -1000),
            HexPoint(-1000, -737)
        )
        result = minContainingHexagon(*points)
        assertEquals(1700, result.radius)
        assertTrue(points.all { result.contains(it) })
    }

}