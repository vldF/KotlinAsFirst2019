@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import kotlin.math.*

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int = (abs(x - other.x) + abs(x + y - other.x - other.y) + abs(y - other.y)) / 2

    override fun toString(): String = "$y.$x"

    fun axialToCube(): Cube = Cube(x * 1.0, -(x + y) * 1.0, y * 1.0)
}

class Cube(val x: Double, val y: Double, val z: Double) {
    fun cubeToAxial() = HexPoint(x.roundToInt(), z.roundToInt())
}

/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {
    /**
     * Средняя
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int = max(0, center.distance(other.center) - radius - other.radius)

    /**
     * Тривиальная
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = (point.distance(center) <= radius)

    fun getRing(): Set<HexPoint> {
        if (radius == 0) return setOf(center)
        val res = mutableSetOf<HexPoint>()
        var currentPoint = center.move(Direction.DOWN_LEFT, radius)
        for (direction in Direction.values()) {
            if (direction == Direction.INCORRECT) break
            for (i in 0 until radius) {
                currentPoint = currentPoint.move(direction, 1)
                res.add(currentPoint)
            }
        }
        return res
    }
}

/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean {
        if (begin.x == end.x || begin.y == end.y) return true
        if (begin.y == end.y) return true
        if (end.x - begin.x == begin.y - end.y) return true
        return false
    }

    /**
     * Средняя
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction {
        // Во всех ветках по два условия для того, чтобы избежать ситуацию с проверкой координат на различие
        if (!isValid()) return Direction.INCORRECT
        if (begin.x == end.x) {
            if (begin.y < end.y) return Direction.UP_RIGHT
            else if (begin.y > end.y) return Direction.DOWN_LEFT
        } else if (begin.y == end.y) {
            if (begin.x < end.x) return Direction.RIGHT
            else if (begin.x > end.x) return Direction.LEFT
        } else if (begin.x + begin.y == end.x + end.y) {
            if (begin.x < end.x) return Direction.DOWN_RIGHT
            else if (begin.x > end.x) return Direction.UP_LEFT
        }
        return Direction.INCORRECT
    }

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction = when (values()[ordinal]) {
        RIGHT -> LEFT
        UP_RIGHT -> DOWN_LEFT
        UP_LEFT -> DOWN_RIGHT
        LEFT -> RIGHT
        DOWN_LEFT -> UP_RIGHT
        DOWN_RIGHT -> UP_LEFT
        else -> INCORRECT
    }


    /**
     * Средняя
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction {
        require(ordinal != 6)
        return values()[(ordinal + 1) % 6]
    }

    /**
     * Простая
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean =
        (ordinal == other.ordinal || ordinal == other.opposite().ordinal) && other.name != "INCORRECT"
}

/**
 * Средняя
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint = when (direction) {
    Direction.RIGHT -> HexPoint(this.x + distance, this.y)
    Direction.LEFT -> HexPoint(this.x - distance, this.y)
    Direction.UP_LEFT -> HexPoint(this.x - distance, this.y + distance)
    Direction.UP_RIGHT -> HexPoint(this.x, this.y + distance)
    Direction.DOWN_RIGHT -> HexPoint(this.x + distance, this.y - distance)
    Direction.DOWN_LEFT -> HexPoint(this.x, this.y - distance)
    else -> throw IllegalArgumentException()
}

/**
 * Сложная
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> {
    val dist = from.distance(to)
    if (dist == 0) return listOf(from)
    val res = mutableListOf<HexPoint>()

    for (i in 0..dist) {
        res.add(cubeRound(cubeLerp(from.axialToCube(), to.axialToCube(), 1.0 * i / dist)).cubeToAxial())
    }

    return res
}

fun cubeRound(cube: Cube): Cube {
    var rx = round(cube.x)
    var ry = round(cube.y)
    var rz = round(cube.z)

    val xDiff = abs(rx - cube.x)
    val yDiff = abs(ry - cube.y)
    val zDiff = abs(rz - cube.z)

    if (xDiff > yDiff && xDiff > zDiff) {
        rx = -ry - rz
    } else if (yDiff > zDiff) {
        ry = -rx - rz
    } else {
        rz = -rx - ry
    }
    return Cube(rx, ry, rz)
}

fun cubeLerp(a: Cube, b: Cube, t: Double) = Cube(lerp(a.x, b.x, t), lerp(a.y, b.y, t), lerp(a.z, b.z, t))

fun lerp(a: Double, b: Double, t: Double) = a + (b - a) * t


/**
 * Очень сложная
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */
fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    val maxRadius = maxOf(a.distance(b), b.distance(c), c.distance(a))
    if (HexSegment(a, b).direction().isParallel(HexSegment(b, c).direction())) {
        // Точки на одной прямой, ответ -- шестиугольник с радиусом, равному наибольшему расстоянию между точками
        // И центром на пересечении колец вокруг "крайних" точек
        return findIntersect(a, b, c, maxRadius = maxRadius + 1, minRadius = maxRadius, count = 2)
    }

    return findIntersect(a, b, c, maxRadius = maxRadius, count = 1, minRadius = maxRadius / 2)
}

/**
 * Поиск таких точек (size=count), что расстояние от этих точек до всех точек из centers равно
 *
 * В теории, можно попробовать решить аналитически систему из трёх уравнений с модулем, но это нереально сложно
 * и код будет еще больше, чем тут
 */
fun findIntersect(vararg centers: HexPoint, maxRadius: Int, minRadius: Int = 0, count: Int): Hexagon? {
    for (r in minRadius..maxRadius) {
        val unions = mutableListOf<Set<HexPoint>>()
        for (center in centers) {
            val hexSet = Hexagon(center, r).getRing()
            unions.add(hexSet)
        }
        var intersect = unions.first()
        for (hexSet in unions) {
            intersect = intersect.intersect(hexSet)
        }
        if (intersect.size == count)
            return Hexagon(intersect.first(), max(1, intersect.first().distance(centers.first())))
    }
    return null
}

/**
 * Очень сложная
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */
fun minContainingHexagon(vararg points: HexPoint): Hexagon {
    var currentRadius = 0
    while (true) {
        val unionPoints = findMultipyHexPoints(*points, Radius = currentRadius)
        for (p in unionPoints) {
            val hexagon = Hexagon(p, currentRadius)
            if (points.all { hexagon.contains(it) }) return hexagon
        }
        currentRadius++
    }
}

fun findMultipyHexPoints(vararg centers: HexPoint, Radius: Int): Set<HexPoint> {
    val unions = mutableListOf<Set<HexPoint>>()
    for (center in centers) {
        val hexSet = Hexagon(center, Radius).getRing()
        unions.add(hexSet)
    }
    val allHexPoints = mutableListOf<HexPoint>()
    val res = mutableSetOf<HexPoint>()
    for (hexSet in unions) {
        allHexPoints.addAll(hexSet)
    }
    for (hexSet in unions) {
        for (point in hexSet) {
            if (allHexPoints.count { it == point } > 1) {
                res.add(point)
            }
        }
    }
    return res

}



