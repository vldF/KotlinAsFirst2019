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
                Point(2.220446049250313e-16, 0.40859240176324674),
                Point(0.2276525073004041, -632.0),
                Point(0.0, -632.0),
                Point(2.220446049250313e-16, 0.3342909930854997),
                Point(-632.0, 0.6237174886906942),
                Point(2.220446049250313e-16, 0.14818814588114337),
                Point(2.220446049250313e-16, 0.0625237647866781),
                Point(0.5906138559652889, 0.26529281481809197),
                Point(-5e-324, -632.0),
                Point(0.27605376286731975, 0.6033728491366848),
                Point(0.4083807002612172, 0.0),
                Point(0.9126201172869807, -5e-324),
                Point(5e-324, 5e-324),
                Point(0.41801992091719764, 0.5851269525531354),
                Point(0.40827560317639455, 5e-324),
                Point(0.4586108545381111, 0.6293756368909378),
                Point(-2.220446049250313e-16, 0.07257378413809357),
                Point(-5e-324, 0.0),
                Point(2.220446049250313e-16, 0.0),
                Point(0.5489886426384738, 0.43668188989856005),
                Point(0.458766798613252, 0.5020528711767636),
                Point(-5e-324, 0.07035748656416985),
                Point(0.32228634543769885, -5e-324),
                Point(-632.0, -632.0),
                Point(-632.0, 0.6852241078116675),
                Point(0.0, 0.5958193053512483),
                Point(-632.0, -2.220446049250313e-16),
                Point(2.220446049250313e-16, -632.0),
                Point(0.8473335786636286, 5e-324),
                Point(-632.0, 0.286655257212701),
                Point(-5e-324, 0.0),
                Point(-5e-324, 0.4631316434773852),
                Point(0.6911527033286813, 0.5275883219157836),
                Point(0.15558141975865003, 0.14183967858582447),
                Point(5e-324, 0.4826441896066913),
                Point(0.486152762852758, -632.0),
                Point(-2.220446049250313e-16, -632.0),
                Point(-632.0, -632.0),
                Point(0.0, 0.14010108981764602),
                Point(-632.0, -2.220446049250313e-16),
                Point(0.0, 0.49288105289010653),
                Point(0.5438687005344023, 0.984853425749717),
                Point(5e-324, 0.41755857850154454),
                Point(0.9037695677831018, -5e-324),
                Point(0.9832816029363238, -632.0),
                Point(-5e-324, 0.14521301095929695),
                Point(0.6508921561197156, 0.0),
                Point(2.220446049250313e-16, -632.0),
                Point(0.7736311725158087, 0.18645059378820472),
                Point(0.5655182112143275, -632.0),
                Point(-632.0, 0.8330358024952728),
                Point(-2.220446049250313e-16, -632.0),
                Point(5e-324, 0.48001565437879734),
                Point(0.8786154341262933, 0.11694663954211737),
                Point(0.6476312140962198, 0.6112419196462591),
                Point(-5e-324, 0.15994352605229523),
                Point(0.6949879928844629, 0.7922623462443996),
                Point(0.2762882592782674, -632.0),
                Point(0.2999424231941852, 0.0),
                Point(0.0, 0.0),
                Point(0.2769563834039138, 0.9084237232544393),
                Point(5e-324, -5e-324),
                Point(0.9319626061441764, -632.0),
                Point(-632.0, 0.0),
                Point(0.5131478174232063, -5e-324),
                Point(0.10510565346461365, 0.5647067412824601),
                Point(0.02365358582136068, 0.23453024489719176),
                Point(0.7907523344410747, 5e-324),
                Point(-632.0, 0.9104502679729505),
                Point(0.0, 0.13750791534793616),
                Point(5e-324, -632.0),
                Point(0.9491213213040002, 0.0455072207349686),
                Point(-2.220446049250313e-16, 0.6907397186382604),
                Point(-632.0, 0.37501538782260957),
                Point(0.19132433972739216, 2.220446049250313e-16),
                Point(-632.0, 5e-324),
                Point(0.9151117325309196, 0.4711836388272762),
                Point(0.9509033283879962, -632.0),
                Point(-632.0, 0.36495904642935173),
                Point(-5e-324, 0.9593430495889483),
                Point(2.220446049250313e-16, 0.21158482767811948),
                Point(0.13188619838645055, 0.9137279671666096),
                Point(-5e-324, 0.0),
                Point(0.03267513693675139, 2.220446049250313e-16),
                Point(-2.220446049250313e-16, -632.0),
                Point(0.962263692030891, -632.0),
                Point(-632.0, -632.0),
                Point(2.220446049250313e-16, 0.739982206114079),
                Point(0.1870825454282683, 0.5604090925095708),
                Point(0.19850444302142745, 0.43110146222472234),
                Point(-632.0, 5e-324),
                Point(0.9067052237998213, 2.220446049250313e-16),
                Point(0.9324783477359869, -5e-324)
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

        result = circleByThreePoints(Point(-632.0, 2.220446049250313e-16), Point(0.5587931247116411, 5e-324), Point(0.18952233758553172, -632.0))
        assertTrue(result.center.distance(Point(-315.7206034376442, -315.8153092385075)) < 1e-5)
        assertEquals(446.9585732920469, result.radius, 1e-5)
    }

    @Test
    @Tag("Impossible")
    fun minContainingCircle() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        val result = minContainingCircle(p1, p2, p3, p4, p5, p6)
        assertEquals(4.0, result.radius, 0.02)
        for (p in listOf(p1, p2, p3, p4, p5, p6)) {
            assertTrue(result.contains(p))
        }

    }
}