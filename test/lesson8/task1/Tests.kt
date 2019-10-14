package lesson8.task1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.Math.ulp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt

class Tests {
    @Test
    @Tag("Example")
    fun pointDistance() {
        assertEquals(0.0, Point(0.0, 0.0).distance(Point(0.0, 0.0)), 1e-5)
        assertEquals(5.0, Point(3.0, 0.0).distance(Point(0.0, 4.0)), 1e-5)
        assertEquals(50.0, Point(0.0, -30.0).distance(Point(-40.0, 0.0)), 1e-5)
    }

    @Test
    @Tag("Example")
    fun halfPerimeter() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).halfPerimeter(), 1e-5)
        assertEquals(2.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).halfPerimeter(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleArea() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).area(), 1e-5)
        assertEquals(0.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).area(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleContains() {
        assertTrue(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(1.5, 1.5)))
        assertFalse(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(2.5, 2.5)))
    }

    @Test
    @Tag("Example")
    fun segmentEquals() {
        val first = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val second = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val third = Segment(Point(3.0, 4.0), Point(1.0, 2.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
    }

    private fun approxEquals(expected: Line, actual: Line, delta: Double): Boolean =
        abs(expected.angle - actual.angle) <= delta && abs(expected.b - actual.b) <= delta

    private fun assertApproxEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun assertApproxNotEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertFalse(approxEquals(expected, actual, delta))
    }

    @Test
    @Tag("Example")
    fun lineEquals() {
        run {
            val first = Line(Point(0.0, 0.0), 0.0)
            val second = Line(Point(3.0, 0.0), 0.0)
            val third = Line(Point(-5.0, 0.0), 0.0)
            val fourth = Line(Point(3.0, 1.0), 0.0)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 2)
            val second = Line(Point(0.0, 3.0), PI / 2)
            val third = Line(Point(0.0, -5.0), PI / 2)
            val fourth = Line(Point(1.0, 3.0), PI / 2)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 4)
            val second = Line(Point(3.0, 3.0), PI / 4)
            val third = Line(Point(-5.0, -5.0), PI / 4)
            val fourth = Line(Point(3.00001, 3.0), PI / 4)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
    }

    @Test
    @Tag("Example")
    fun triangleEquals() {
        val first = Triangle(Point(0.0, 0.0), Point(3.0, 0.0), Point(0.0, 4.0))
        val second = Triangle(Point(0.0, 0.0), Point(0.0, 4.0), Point(3.0, 0.0))
        val third = Triangle(Point(0.0, 4.0), Point(0.0, 0.0), Point(3.0, 0.0))
        val fourth = Triangle(Point(0.0, 4.0), Point(0.0, 3.0), Point(3.0, 0.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
        assertNotEquals(fourth, first)
    }

    @Test
    @Tag("Easy")
    fun circleDistance() {
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(1.0, 0.0), 1.0)), 1e-5)
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(0.0, 2.0), 1.0)), 1e-5)
        assertEquals(1.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(-4.0, 0.0), 2.0)), 1e-5)
        assertEquals(2.0 * sqrt(2.0) - 2.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(2.0, 2.0), 1.0)), 1e-5)
    }

    @Test
    @Tag("Trivial")
    fun circleContains() {
        assertFalse(
            Circle(Point(0.9186370043116991, -2.220446049250313e-16), 0.10000000000000023).contains(
                Point(
                    0.8658000603471704,
                    0.17355241917149467
                )
            )
        )
        val center = Point(1.0, 2.0)
        assertTrue(Circle(center, 1.0).contains(center))
        assertFalse(Circle(center, 2.0).contains(Point(0.0, 0.0)))
        assertTrue(Circle(Point(0.0, 3.0), 5.01).contains(Point(-4.0, 0.0)))
    }

    @Test
    @Tag("Normal")
    fun diameter() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        assertEquals(Segment(p5, p6), diameter(p1, p2, p3, p4, p5, p6))
        assertEquals(Segment(p4, p6), diameter(p1, p2, p3, p4, p6))
        assertEquals(Segment(p3, p4), diameter(p1, p2, p3, p4))
        assertEquals(Segment(p2, p4), diameter(p1, p2, p4))
        assertEquals(Segment(p1, p4), diameter(p1, p4))
        assertEquals(
            895.0210424165128, diameter(
                Point(0.4610824682143857, 0.005505286500788409),
                Point(0.0, 0.0),
                Point(-632.0, -2.220446049250313e-16),
                Point(0.8981483887634799, 5e-324),
                Point(0.0, -632.0),
                Point(0.10648992542646085, 0.6748573898608264),
                Point(2.220446049250313e-16, -5e-324),
                Point(0.6620972780283597, -5e-324),
                Point(0.02747157889578833, 0.7687462281497206),
                Point(5e-324, -632.0),
                Point(-632.0, 0.06686906144218274),
                Point(0.0, 0.17327724477042716),
                Point(-632.0, 0.9323020614083327),
                Point(5e-324, 0.5120433014582049),
                Point(0.8473710590694817, 0.724431679611259),
                Point(0.27972852430192807, 0.0),
                Point(0.6730106987676912, 5e-324),
                Point(-632.0, 0.0),
                Point(-632.0, 0.3403939734464897),
                Point(0.0736744505470569, -632.0),
                Point(-632.0, 0.0),
                Point(2.220446049250313e-16, 0.0),
                Point(-632.0, 0.43924604154965585),
                Point(5e-324, 0.4686611024842491),
                Point(-632.0, 0.3685255040092783),
                Point(5e-324, -632.0),
                Point(-632.0, 0.04380757745315367),
                Point(-632.0, 0.25637663800622046),
                Point(0.9380988536607022, 0.18992386922485738),
                Point(0.6386187747177592, -5e-324),
                Point(2.220446049250313e-16, 5e-324),
                Point(0.8971887211900841, 0.31590189705217786),
                Point(0.303758448936282, -632.0),
                Point(-632.0, 0.0),
                Point(0.5032704332094795, 0.8227301159785952),
                Point(0.3632759648270544, 0.13276487657824598),
                Point(0.09421594755970408, 2.220446049250313e-16),
                Point(0.6336879536504775, -632.0),
                Point(-5e-324, 0.6640949306617805),
                Point(0.5609043740270735, 0.10933852694097457),
                Point(0.41156666188463287, 0.32969273321211234),
                Point(0.6090301196553293, 0.2993693105969516),
                Point(0.5258951641906933, 0.7565085075244808),
                Point(-2.220446049250313e-16, 0.30440871075339127),
                Point(0.18903055795895451, 5e-324),
                Point(0.18562995121562154, -632.0),
                Point(0.32981621292473495, 0.5943743759043791),
                Point(-632.0, 5e-324),
                Point(-2.220446049250313e-16, 0.7303421656623847),
                Point(2.220446049250313e-16, -632.0),
                Point(0.18876037659484324, 0.015217469060489375),
                Point(0.8185896254845523, -632.0),
                Point(-5e-324, -632.0)
            ).len()
        )
    }

    @Test
    @Tag("Easy")
    fun circleByDiameter() {
        assertEquals(Circle(Point(0.0, 1.0), 1.0), circleByDiameter(Segment(Point(0.0, 0.0), Point(0.0, 2.0))))
        assertEquals(Circle(Point(2.0, 1.5), 2.5), circleByDiameter(Segment(Point(4.0, 0.0), Point(0.0, 3.0))))
    }

    @Test
    @Tag("Normal")
    fun crossPoint() {
        assertTrue(
            Point(2.0, 3.0).distance(
                Line(Point(2.0, 0.0), PI / 2).crossPoint(
                    Line(Point(0.0, 3.0), 0.0)
                )
            ) < 1e-5
        )
        assertTrue(
            Point(2.0, 2.0).distance(
                Line(Point(0.0, 0.0), PI / 4).crossPoint(
                    Line(Point(0.0, 4.0), 3 * PI / 4)
                )
            ) < 1e-5
        )
        val p = Point(1.0, 3.0)
        assertTrue(p.distance(Line(p, 1.0).crossPoint(Line(p, 2.0))) < 1e-5)
    }

    @Test
    @Tag("Normal")
    fun lineBySegment() {
        assertApproxEquals(Line(Point(0.0, 0.0), 0.0), lineBySegment(Segment(Point(0.0, 0.0), Point(7.0, 0.0))))
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineBySegment(Segment(Point(0.0, 0.0), Point(0.0, 8.0))))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineBySegment(Segment(Point(1.0, 1.0), Point(3.0, 3.0))))
    }

    @Test
    @Tag("Normal")
    fun lineByPoint() {
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineByPoints(Point(0.0, 0.0), Point(0.0, 2.0)))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineByPoints(Point(1.0, 1.0), Point(3.0, 3.0)))
    }

    @Test
    @Tag("Hard")
    fun bisectorByPoints() {
        assertApproxEquals(
            Line(315.7674369247018, 1.5707963267948966),
            bisectorByPoints(Point(0.46512615059631457, -2.220446049250313e-16), Point(-632.0, -5e-324))
        )
        assertApproxEquals(Line(Point(2.0, 0.0), PI / 2), bisectorByPoints(Point(0.0, 0.0), Point(4.0, 0.0)))
        assertApproxEquals(Line(Point(1.0, 2.0), 0.0), bisectorByPoints(Point(1.0, 5.0), Point(1.0, -1.0)))
    }

    @Test
    @Tag("Normal")
    fun findNearestCirclePair() {
        val c1 = Circle(Point(0.0, 0.0), 1.0)
        val c2 = Circle(Point(3.0, 0.0), 5.0)
        val c3 = Circle(Point(-5.0, 0.0), 2.0)
        val c4 = Circle(Point(0.0, 7.0), 3.0)
        val c5 = Circle(Point(0.0, -6.0), 4.0)
        assertEquals(Pair(c1, c5), findNearestCirclePair(c1, c3, c4, c5))
        assertEquals(Pair(c2, c4), findNearestCirclePair(c2, c4, c5))
        assertEquals(Pair(c1, c2), findNearestCirclePair(c1, c2, c4, c5))
    }

    @Test
    @Tag("Hard")
    fun circleByThreePoints() {
        var result = circleByThreePoints(Point(5.0, 0.0), Point(3.0, 4.0), Point(0.0, -5.0))
        assertTrue(result.center.distance(Point(0.0, 0.0)) < 1e-5)
        assertEquals(5.0, result.radius, 1e-5)

        result = circleByThreePoints(
            Point(-632.0, 2.220446049250313e-16),
            Point(0.5587931247116411, 5e-324),
            Point(0.18952233758553172, -632.0)
        )
        assertTrue(result.center.distance(Point(-315.7206034376442, -315.8153092385075)) < 1e-5)
        assertEquals(446.9585732920469, result.radius, 1e-5)
    }

    @Test
    @Tag("Impossible")
    fun minContainingCircle() {
        val p1 = Point(-2.220446049250313e-16, -632.0)
        val p2 = Point(0.21324838408024116, -632.0)
        val p3 = Point(0.2247640966272566, -632.0)
        val p4 = Point(2.220446049250313e-16, -2.220446049250313e-16)
        val p5 = Point(0.7330468201116503, 0.30041682315821094)
        val p6 = Point(0.2208629115859423, 0.24663525363712124)
        val p7 = Point(0.4828109698653852, 0.0)
        val p8 = Point(0.8599415840653242, -2.220446049250313e-16)
        val p9 = Point(0.13278748498413062, 0.9985131945833865)
        val result = minContainingCircle(p1, p2, p3, p4, p5, p6, p7, p8, p9)
        //assertEquals(4.0, result.radius, 0.02)
        for (p in listOf(p1, p2, p3, p4, p5, p6)) {
            assertTrue(result.contains(p))
        }

    }
}