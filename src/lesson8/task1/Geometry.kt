@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException
import kotlin.math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {

    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))

    override fun equals(other: Any?): Boolean = other is Point && other.x == x && other.y == y
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = max(0.0, center.distance(other.center) - radius - other.radius)

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     * И всё таки, модуль тут не нужен
     */
    fun contains(p: Point): Boolean = center.distance(p) - radius <= 1e-5
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()

    fun len() = begin.distance(end)
    fun middle() = Point((begin.x + end.x) / 2, (begin.y + end.y) / 2)
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg pts: Point): Segment {
    // Алгоритм Джарвиса. Он обращается ко всем на "Сер"
    require(pts.isNotEmpty())

    val points = pts.toSet().toList().shuffled().toMutableList()

    // Найдём стартовую точку
    // Не просто ищем, а переставляем ее в самое начало списка
    // Это нужно для успешного выхода из цикла ниже
    for (pIdx in points.indices) {
        if (points[pIdx].x < points[0].x || points[pIdx].x == points[0].x && points[pIdx].y < points[0].y) {
            points[0] = points[pIdx].also { points[pIdx] = points[0] }
        }
    }
    val startPoint = points[0]
    // Поиск второй точки
    var secondPoint = Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
    for (pIdx in 1 until points.size) {
        if (points[pIdx].y < secondPoint.y || points[pIdx].y == secondPoint.y && points[pIdx].x < points[0].x) {
            secondPoint = points[pIdx]
        }
    }

    val listBorderPoints = mutableListOf(startPoint)
    listBorderPoints.add(secondPoint)
    var lastPoint = secondPoint
    var maxDist = -1.0
    var pairOfFarthestPoints = Point(-1.0, -1.0) to Point(-1.0, -1.0)
    while (startPoint != lastPoint) {
        secondPoint = lastPoint
        var cos = -2.0
        var nextPoint = Point(-1.0, -1.0)
        for (p in points) {
            val currentCos = cosLines(listBorderPoints[listBorderPoints.size - 2], secondPoint, p)
            if (currentCos > cos) {
                cos = currentCos
                nextPoint = p
            }
        }
        val (newMaxDist, newPair) = findMaxDist(listBorderPoints, nextPoint)
        if (newMaxDist > maxDist) {
            maxDist = newMaxDist
            pairOfFarthestPoints = newPair
        }
        lastPoint = nextPoint
        listBorderPoints.add(nextPoint)
    }

    return Segment(pairOfFarthestPoints.first, pairOfFarthestPoints.second)
}

fun findMaxDist(points: List<Point>, point: Point): Pair<Double, Pair<Point, Point>> {
    var dist = -1.0
    var neededPoint = Point(-1.0, -1.0)
    for (p in points) {
        val newDist = p.distance(point)
        if (newDist > dist) {
            dist = newDist
            neededPoint = p
        }
    }
    return (dist to (point to neededPoint))
}

/**
 * Косинус угла между прямыми AB и BC
 */
fun cosLines(a: Point, b: Point, c: Point): Double {
    val v1 = Point(b.x - a.x, b.y - a.y)
    val v2 = Point(c.x - b.x, c.y - b.y)
    val dot = v1.x * v2.x + v1.y * v2.y
    val absFirst = sqrt(v1.x * v1.x + v1.y * v1.y)
    val absSecond = sqrt(v2.x * v2.x + v2.y * v2.y)

    return dot / absFirst / absSecond
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(diameter.middle(), diameter.len() / 2)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val x = calcX(other.angle, other.b)
        return Point(x, calcY(x, other.angle, other.b))
    }

    private fun calcX(beta: Double, b2: Double) = (cos(angle) * b2 - b * cos(beta)) / sin(angle - beta)

    private fun calcY(x: Double, beta: Double, b2: Double): Double =
        if (angle == Math.PI / 2) (x * sin(beta) + b2) / cos(beta) else (x * sin(angle) + b) / cos(angle)

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = lineByPoints(s.begin, s.end)

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    if (a.x == b.x) return Line(a, Math.PI / 2)

    val k = (a.y - b.y) / (a.x - b.x)
    return if (k >= 0) {
        Line(a, atan(k) % PI)
    } else {
        Line(a, (PI - atan(-k)) % PI)
    }
}

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val line = lineByPoints(a, b)
    val alpha = line.angle + PI / 2
    return Line(Segment(a, b).middle(), alpha % PI)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    require(circles.size >= 2)
    // Не придумал ничего лучше обычного перебора за O(n^2)
    var minDist = Double.POSITIVE_INFINITY
    var res = Circle(Point(0.0, 0.0), 0.0) to Circle(Point(0.0, 0.0), 0.0)
    for (firstIdx in circles.indices) {
        for (secondIdx in firstIdx + 1 until circles.size) {
            val d = circles[firstIdx].distance(circles[secondIdx])
            if (d < minDist) {
                minDist = d
                res = circles[firstIdx] to circles[secondIdx]
            }
            if (d == 0.0) return res
        }
    }
    return res
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val firstLine = bisectorByPoints(a, b)
    val secondLine = bisectorByPoints(a, c)
    val center = firstLine.crossPoint(secondLine)
    val r = center.distance(a)

    return Circle(center, r)
}

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg pts: Point): Circle {
    require(pts.isNotEmpty())
    if (pts.size == 1) return Circle(pts[0], 0.0)
    // Алгоритм с neerc.ifmo.ru
    val points = pts.toMutableList()
    val addedPoints = mutableListOf<Point>()
    var circle = circleByDiameter(Segment(points[0], points[1]))

    addedPoints.add(points[0])
    addedPoints.add(points[1])

    for (idx in 2 until points.size) {
        if (!circle.contains(points[idx])) {
            circle = minCircleWithPoint(addedPoints, points[idx])
        }
        addedPoints.add(points[idx])
    }
    return circle
}

fun minCircleWithPoint(points: List<Point>, point: Point): Circle {
    val shuffled = points
    var circle = circleByDiameter(Segment(points[0], point))
    val addedPoints = mutableListOf<Point>()
    addedPoints.add(shuffled[0])
    for (idx in 1 until shuffled.size) {
        if (!circle.contains(shuffled[idx])) {
            circle = minCircleWithTwoPoint(addedPoints, shuffled[idx], point)
        }
        addedPoints.add(shuffled[idx])
    }
    return circle
}

fun minCircleWithTwoPoint(points: List<Point>, pointFirst: Point, pointSecond: Point): Circle {
    var circle = circleByDiameter(Segment(pointFirst, pointSecond))
    for (point in points) {
        if (!circle.contains(point)) circle = circleByThreePoints(point, pointFirst, pointSecond)
    }
    return circle
}

