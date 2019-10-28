@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.Cell
import lesson9.task1.Matrix
import lesson9.task1.MatrixImpl
import lesson9.task1.createMatrix
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.abs

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = MatrixImpl(height, width, 0)
    var idxRow = 0
    var idxCol = 0
    var step = 1
    var direction = 0 // 0 = >, 1 = \/, 2 = <, 3 = /\
    var count = width
    var distRow = height - 1
    var distCol = width - 1
    while (true) {
        matrix[idxRow, idxCol] = step
        step++
        count--
        if (count == 0) {
            if (direction % 2 == 0) {
                count = distRow
                distRow--
            } else {
                count = distCol
                distCol--
            }
            direction = (direction + 1) % 4
        }
        when (direction) {
            0 -> idxCol++
            1 -> idxRow++
            2 -> idxCol--
            3 -> idxRow--
        }
        if (step == height * width + 1) return matrix
    }
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    var boxColStart = 0
    var boxRowStart = 0
    var boxColEnd = width - 1
    var boxRowEnd = height - 1

    val res = MatrixImpl(height, width, 0)
    var count = 1
    while (true) {
        for (y in boxColStart..boxColEnd) {
            for (x in boxRowStart..boxRowEnd) {
                res[x, y] = count
            }
        }
        if (boxRowEnd - boxRowStart <= 1 || boxColEnd - boxColStart <= 1) return res
        count++
        boxColStart++
        boxRowStart++
        boxColEnd--
        boxRowEnd--
    }


}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    var idxCol = 0
    var idxRow = 0
    var step = 0
    var n = 1
    val res = MatrixImpl(height, width, 0)
    while (true) {
        if (idxRow == height) {
            step++
            idxCol = step
            idxRow = 0
        }
        if (idxCol !in 0 until width || idxRow !in 0 until height) {
            idxCol--
            idxRow++
            continue
        }
        res[idxRow, idxCol] = n
        n++

        if (idxCol == width - 1 && idxRow == height - 1) return res
        idxCol--
        idxRow++
    }
}

/**
 * Средняя
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    val s = matrix.height
    val res = MatrixImpl(s, s, matrix[0, 0])
    for (x in 0 until s) {
        for (y in 0 until s) {
            res[x, s - y - 1] = matrix[y, x]
        }
    }
    return res
}

/**
 * Сложная
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean =
    matrix.height == matrix.width && isLatin(matrix) && isLatin(rotate(matrix))

fun isLatin(matrix: Matrix<Int>): Boolean {
    for (x in 0 until matrix.height) {
        val s = mutableListOf<Int>()
        for (i in 0 until matrix.height) s.add(0)
        for (y in 0 until matrix.height) {
            val t = matrix[x, y] - 1
            if (t !in 0 until matrix.height) return false
            s[t] += 1
        }
        if (s.min() == 0) return false
    }
    return true
}

/**
 * Средняя
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    val res = MatrixImpl(matrix.height, matrix.width, 0)
    for (x in 0 until matrix.width) {
        for (y in 0 until matrix.height) {
            res[y, x] = findNeighbours(x, y, matrix).sum()
        }
    }
    return res
}

fun findNeighbours(x: Int, y: Int, matrix: Matrix<Int>): MutableList<Int> {
    val s = mutableListOf<Int>()
    for (j in listOf(x - 1, x, x + 1)) {
        for (i in listOf(y - 1, y, y + 1)) {
            if (i == y && j == x) continue
            s.add(matrix.saftyGet(i, j, 0))
        }
    }
    return s
}


/**
 * Средняя
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val cols = mutableListOf<Int>()
    for (i in 0 until matrix.width) {
        if (matrix.getCol(i).max() == 0) cols.add(i)
    }
    for (i in 0 until matrix.height) {
        if (matrix.getRow(i).max() == 0) rows.add(i)
    }
    return Holes(rows, cols)
}


/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val res = MatrixImpl(matrix.height, matrix.width, 0)
    for (y in 0 until matrix.height) {
        for (x in 0 until matrix.width) {
            res[y, x] = matrix.getLeftSum(x, y).sum()
        }
    }
    return res
}

/**
 * Сложная
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    for (x in 0 until lock.width) {
        for (y in 0 until lock.height) {
            if (lock.partyEquals(key, y, x)) return Triple(true, y, x)
        }
    }
    return Triple(false, -1, -1)
}

/**
 * Простая
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    val res = MatrixImpl(height, width, -1)
    for (i in 0 until height) {
        for (j in 0 until width) {
            res[i, j] = -this[i, j]
        }
    }
    return res
}


/**
 * Средняя
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    val res = MatrixImpl(height, other.width, 0)
    for (y in 0 until height) {
        for (x in 0 until other.width) {
            res[y, x] = lesson4.task1.times(getRow(y), other.getCol(x))
        }
    }
    return res
}

/**
 * Сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    val titles = mutableMapOf<Int, Cell>()
    for (x in 0 until matrix.width) {
        for (y in 0 until matrix.height) {
            titles[matrix[y, x]] = Cell(y, x)
        }
    }

    for (i in moves) {
        val coordsMovingTitle = titles[i] ?: throw IllegalStateException()
        check(findTrueNeighbours(coordsMovingTitle.column, coordsMovingTitle.row, matrix).contains(0))
        matrix[titles[0]!!] = i.also { matrix[coordsMovingTitle] = 0 }
        titles[0] = coordsMovingTitle.also { titles[i] = titles[0]!! }
    }
    return matrix
}

fun findTrueNeighbours(x: Int, y: Int, matrix: Matrix<Int>): MutableList<Int> {
    val s = mutableListOf<Int>()
    s.add(matrix.saftyGet(y - 1, x, -1))
    s.add(matrix.saftyGet(y + 1, x, -1))
    s.add(matrix.saftyGet(y, x - 1, -1))
    s.add(matrix.saftyGet(y, x + 1, -1))
    return s
}

/**
 * Очень сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    val listOfOpenedStates = PriorityQueue<GameState>(compareBy { it.f })
    val setOfOpenedFields = mutableSetOf<Matrix<Int>>()
    val setOfClosedFields = mutableSetOf<Matrix<Int>>()

    val referenceFirst = createMatrix(4, 4, -1)
    for (x in 0..3) {
        for (y in 0..3) {
            referenceFirst[y, x] = y * 4 + x + 1
        }
    }
    val referenceSecond = referenceFirst.matrixCopy()
    referenceFirst[3, 3] = 0
    referenceSecond[3, 3] = 0
    referenceSecond[3, 1] = 15
    referenceSecond[3, 2] = 14
    if (matrix == referenceFirst || matrix == referenceSecond) return listOf()

    var zero = Cell(-1, -1)
    for (x in 0 until 4) {
        for (y in 0 until 4) {
            if (matrix[y, x] == 0) {
                zero = Cell(y, x)
            }
        }
    }
    val firstState = GameState(matrix, zero, mutableListOf())
    listOfOpenedStates.add(firstState)
    while (true) {
        val currentState = listOfOpenedStates.poll()
        setOfClosedFields.add(currentState.field)
        listOfOpenedStates.remove(currentState)
        for (move in currentState.findCellsNeighbours()) {
            val newField = currentState.field.matrixCopy()
            val newMovies = currentState.movies.toMutableList()
            newMovies.add(newField[move])

            // do move and create new GameState
            newField[currentState.zero] = newField[move].also { newField[move] = newField[currentState.zero] }
            if (newField in setOfClosedFields || newField in setOfOpenedFields) {
                continue
            }
            val newState = GameState(newField, move, newMovies)
            newState.calcH()

            if (newField == referenceFirst || newField == referenceSecond) {
                return newState.movies
            }
            listOfOpenedStates.add(newState)
            setOfOpenedFields.add(newField)
        }
    }
}

fun Matrix<Int>.matrixCopy(): Matrix<Int> {
    val res = MatrixImpl(height, width, -1)
    for (i in 0 until height) {
        for (j in 0 until width) {
            res[i, j] = this[i, j]
        }
    }
    return res
}

class GameState(
    val field: Matrix<Int>,
    val zero: Cell,
    val movies: MutableList<Int>
) {
    val f: Int

    init {
        f = calcH()
    }

    fun calcH(): Int {
        var hScore = 0
        for (x in 0 until 4) {
            for (y in 0 until 4) {
                if (field[y, x] != y * 4 + x + 1) {
                    hScore += if (field[y, x] == 0) abs(x - 3) + abs(y - 3)
                    else abs((field[y, x] - 1) % 4 - x) + abs(field[y, x] / 4 - y)
                }
            }
        }
        return hScore
    }

    fun findCellsNeighbours(): MutableList<Cell> {
        val h = 4
        val s = mutableListOf<Cell>()
        if (zero.row - 1 in 0 until h) s.add(Cell(zero.row - 1, zero.column))
        if (zero.row + 1 in 0 until h) s.add(Cell(zero.row + 1, zero.column))
        if (zero.column - 1 in 0 until h) s.add(Cell(zero.row, zero.column - 1))
        if (zero.column + 1 in 0 until h) s.add(Cell(zero.row, zero.column + 1))
        return s
    }
}
