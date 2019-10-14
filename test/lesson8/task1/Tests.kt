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
        assertEquals(
            895.1436409059279, diameter(
                Point(0.014244632272912727, 0.8939819311072635),
                Point(0.0, -632.0),
                Point(-632.0, 0.003570530541087358),
                Point(0.3562468117822485, -632.0),
                Point(0.26473131252241067, 0.47975563062386783),
                Point(0.06378763152653277, 0.2119509473529375),
                Point(-2.220446049250313e-16, 0.0),
                Point(0.7514035778325413, 0.38639400176296046),
                Point(0.27833751951329744, -5e-324),
                Point(0.2725709260807909, -632.0),
                Point(0.8253446727724469, 0.11429182882657751),
                Point(2.220446049250313e-16, -2.220446049250313e-16),
                Point(0.14260651362479881, 0.15231978355998976),
                Point(2.220446049250313e-16, -632.0),
                Point(0.7608390030884997, 0.5657201520765754),
                Point(0.11203686444411542, 0.21400105951510928),
                Point(0.41797648758451056, 0.0),
                Point(-632.0, -632.0),
                Point(0.6005371399437706, 0.0),
                Point(-632.0, 0.6046952222083606),
                Point(0.0, 0.5571598372866431),
                Point(0.2912599761515172, -632.0),
                Point(-632.0, 0.3575200086320003),
                Point(0.8277610258124923, 0.677167899381976),
                Point(-632.0, -5e-324),
                Point(0.697934600785147, 0.9010671250619112),
                Point(0.8700206573225129, 0.9145392135457008),
                Point(0.4825308817892163, 0.15626965501301981),
                Point(0.5673107898468668, 0.24942679289374903),
                Point(0.7787277106408566, 0.7224184291065454),
                Point(0.0, 0.0950822882465796),
                Point(0.9430750491679456, -632.0),
                Point(0.8422573888497227, 0.1817927450049932),
                Point(0.5141516263771168, 0.8316211845553377),
                Point(2.220446049250313e-16, 0.1029563794045939),
                Point(-5e-324, 0.7677666532523522),
                Point(-632.0, 5e-324),
                Point(-632.0, 0.8882969818427017),
                Point(-632.0, -632.0),
                Point(-5e-324, -632.0),
                Point(0.88818200405803, 0.06431663508379226),
                Point(0.0, -2.220446049250313e-16),
                Point(5e-324, -632.0),
                Point(0.3245313158201537, 0.9830830684607325),
                Point(0.0, 0.027647562930621983),
                Point(2.220446049250313e-16, 0.6925833246121202),
                Point(0.0, 0.7248913103086373),
                Point(0.0, 0.9978508125306359),
                Point(-632.0, 0.9812016178868997),
                Point(-5e-324, 0.0),
                Point(0.033115431076777724, -5e-324),
                Point(-2.220446049250313e-16, -2.220446049250313e-16),
                Point(5e-324, -632.0),
                Point(0.021514855869847693, 0.7718123791130764),
                Point(-632.0, 0.4356058346501842),
                Point(0.4694793061981375, 0.781599291582345),
                Point(-632.0, 0.6552444916755473),
                Point(5e-324, 0.8440963335548016),
                Point(5e-324, 5e-324),
                Point(0.0944960355295702, -2.220446049250313e-16),
                Point(2.220446049250313e-16, 2.220446049250313e-16),
                Point(0.34225920391359244, 0.8743791550169632),
                Point(0.15121601854098932, -632.0),
                Point(-5e-324, -632.0),
                Point(-5e-324, 0.6853554521681611),
                Point(0.6433567686010527, 0.5303178902697588),
                Point(2.220446049250313e-16, 0.5794366235412038),
                Point(0.7416426130631243, -632.0),
                Point(0.24498314925186715, 0.8430887121960958),
                Point(0.7051991852656496, -632.0),
                Point(0.7653284909235173, 0.3207254990048063),
                Point(0.0, -632.0),
                Point(0.8290977685108769, 0.7742014589892996),
                Point(0.3794694119040417, 0.5670362331875501),
                Point(0.5629559960534382, 0.15663625773616718),
                Point(-632.0, -632.0),
                Point(-2.220446049250313e-16, 0.08262735127570608),
                Point(0.36927961394302433, 0.0),
                Point(-632.0, 2.220446049250313e-16),
                Point(0.11432907880598098, 0.0),
                Point(-632.0, 0.5540168307216713),
                Point(-632.0, 0.5551310529614374),
                Point(-632.0, -2.220446049250313e-16),
                Point(0.0, 5e-324),
                Point(0.8588372152100067, 0.18862033412707435),
                Point(0.0, 0.6017361582070718),
                Point(0.7567517581714327, 0.17936922681105805),
                Point(0.40160776774738327, -632.0),
                Point(0.8760075022800162, 0.13511453798826445),
                Point(0.0, -632.0),
                Point(0.6566405417084773, -5e-324),
                Point(0.9905601990868257, -5e-324),
                Point(2.220446049250313e-16, 2.220446049250313e-16),
                Point(0.5109572490961946, 5e-324),
                Point(0.9685088059187831, 0.9295093562472704),
                Point(-632.0, 5e-324),
                Point(0.059232353518447245, 0.5142333323445569)
            ).len()
        )
        assertEquals(
            632.8168216150053, diameter(
                Point(5e-324, 0.21307104455857917),
                Point(0.35927714687973766, -632.0),
                Point(0.0, 0.7349524196242867),
                Point(0.1320614486627525, 0.8166601602678407),
                Point(0.5841040589911265, -632.0),
                Point(0.15385386428325043, 0.13068409117751578),
                Point(2.220446049250313e-16, 0.0)
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