@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

import ru.spbstu.wheels.slice
import java.lang.StringBuilder

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /** Данные */
    var data: MutableList<E>

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    fun saftyGet(row: Int, col: Int, default: E): E {
        if (row in 0 until height && col in 0 until width) return get(row, col)
        return default
    }

    fun getCol(col: Int): MutableList<E> {
        val res = mutableListOf<E>()
        for (i in 0 until height) {
            res.add(data[col + i * width])
        }
        return res
    }

    fun getRow(row: Int): MutableList<E> {
        val res = mutableListOf<E>()
        for (i in row * width until (row + 1) * width) {
            res.add(data[i])
        }
        return res
    }

    fun getLeftSum(toRow: Int, toCol: Int): List<E> {
        val res = mutableListOf<E>()
        for (j in 0..toRow) {
            for (i in 0..toCol) {
                res.add(get(i, j))
            }
        }
        return res
    }

    fun partyEquals(other: Matrix<E>, row: Int, col: Int): Boolean {
        var trigger = false
        for (y in 0 until other.width) {
            for (x in 0 until other.height) {
                if (x + col !in 0 until width || y + row !in 0 until height) return false
                trigger = true
                if (other[y, x] == get(y + row, x + col)) return false
            }
        }
        return trigger
    }

    fun swap(c1: Cell, c2: Cell) {
        set(c1, get(c2)).also { set(c2, get(c1)) }
    }

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)

    fun copy(default: E): Matrix<E> {
        val res = MatrixImpl<E>(height, width, default)
        res.data = data.toMutableList()
        return res
    }
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width, e)

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, defaultValue: E) : Matrix<E> {
    override var data = mutableListOf<E>()

    init {
        require(!(height <= 0 || width <= 0))
        data = MutableList(height * width) { defaultValue }
    }

    override fun get(row: Int, column: Int) = data[width * row + column]

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        data[width * row + column] = value
    }

    override fun set(cell: Cell, value: E) = set(cell.row, cell.column, value)

    override fun equals(other: Any?): Boolean {
        if (other !is MatrixImpl<*> || other.width != width || other.height != height) return false
        for (x in 0 until height) {
            for (y in 0 until width) {
                if (get(x, y) != other[x, y]) return false
            }
        }
        return true
    }

    override fun toString(): String {
        val res = StringBuilder()
        for (h in 0 until height) {
            res.append(data.slice(h * width until (h + 1) * width))
        }

        return res.toString()
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        return result
    }
}

