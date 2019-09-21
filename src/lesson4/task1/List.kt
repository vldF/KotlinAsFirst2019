@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import kotlin.math.pow
import kotlin.math.sqrt

// Таблица с записями чисел для последней задачи
val digOne = arrayOf(
    "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять",
    "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать",
    "восемнадцать", "девятнадцать"
)
val digTwo = arrayOf(
    "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"
)
val digThree = arrayOf(
    "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"
)
val digFour = arrayOf(
    "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"
)

// Таблицы для римски чисел
val thousands = listOf("M", "MM", "MMM")
val hundreds = listOf("C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM")
val dozens = listOf("X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC")
val units = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX")

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.toLowerCase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double =
    sqrt(v.fold(0.0) { previousResult, element -> previousResult + element * element })

/**
 * Простая
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double = if (list.isNotEmpty()) list.sum() / list.size else 0.0

/**
 * Средняя
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    val m = mean(list)
    for (i in list.indices) list[i] -= m
    return list
}

/**
 * Средняя
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int =
    (a.indices).fold(0) { previousResult, element -> previousResult + a[element] * b[element] }

/**
 * Средняя
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */
fun polynom(p: List<Int>, x: Int): Int =
    (p.indices).fold(0) { res, i -> res + p[i] * (x * 1.0).pow(i * 1.0).toInt() }

/**
 * Средняя
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun accumulate(list: MutableList<Int>): MutableList<Int> {
    var s = 0
    for (i in 0 until list.size) {
        val j = list[i]
        list[i] += s
        s += j
    }
    return list
}

/**
 * Средняя
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */

fun factorize(n: Int): List<Int> {
    val res = mutableListOf<Int>()
    var divisor = 3
    var num = n
    while (num % 2 == 0) {
        num /= 2
        res.add(2)
    }
    while (num > 1) {
        while (num % divisor == 0) {
            num /= divisor
            res.add(divisor)
        }
        divisor += 2
    }
    return res
}

/**
 * Сложная
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String = factorize(n).joinToString(separator = "*")

/**
 * Средняя
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    var num = n
    val res = mutableListOf<Int>()
    do {
        res.add(num % base)
        num /= base
    } while (num != 0)
    return res.reversed()
}

/**
 * Сложная
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String =
    convert(n, base).map { if (it < 10) '0' + it else 'a' + (it - 10) }.joinToString(separator = "")

/**
 * Средняя
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int =
    (digits.indices).fold(0) { res, i -> res * base + digits[i] }

/**
 * Сложная
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */
fun decimalFromString(str: String, base: Int): Int {
    var res = 0
    for (i in str.indices) {
        res *= base
        res += if (str[i] in 'a'..'z') str[i] - 'a' + 10 else str[i] - '0'
    }
    return res
}

/**
 * Сложная
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */
fun roman(n: Int): String {
    var num = n
    val res = StringBuilder()
    if (num >= 1000) {
        val firstDigit = n / 1000 % 10
        res.append(thousands[firstDigit - 1])
        num -= 1000 * firstDigit
    }
    if (num >= 100) {
        val secondDigit = n / 100 % 10
        res.append(hundreds[secondDigit - 1])
        num -= 100 * secondDigit
    }
    if (num >= 10) {
        val thirtDigit = n / 10 % 10
        res.append(dozens[thirtDigit - 1])
        num -= 10 * thirtDigit
    }
    if (num > 0) {
        val lastDigit = n % 10
        res.append(units[lastDigit - 1])
    }
    return res.toString()
}

/**
 * Очень сложная
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
fun russian(n: Int): String {
    val first = getTriad(n / 1000, true).toMutableList()
    first.add(trueThous(n / 1000))
    val last = getTriad(n % 1000, false)
    val union = if (n > 1000) first + last else last
    return union.joinToString(separator = " ")
}

fun getTriad(n: Int, thous: Boolean): List<String> {
    val res = mutableListOf<String>()
    val hundreds = n / 100
    val dozens = n / 10 % 10
    val units = n % 10
    val lastTwoDigits = n % 100

    if (hundreds != 0) {
        res.add(digThree[hundreds - 1])
    }
    if (lastTwoDigits in 3..19 && thous || lastTwoDigits in 1..19 && !thous) {
        res.add(digOne[lastTwoDigits - 1])
        return res
    } else if (lastTwoDigits in 1..2) {
        res.add(digFour[lastTwoDigits - 1])
        return res
    }
    if (dozens != 0) {
        res.add(digTwo[dozens - 1])
    }
    if (units != 0) {
        res.add(if (thous) digFour[units - 1] else digOne[units - 1])
    }
    return res
}

fun trueThous(num: Int): String {
    val mod1 = num % 100
    val mod2 = mod1 % 10
    return when {
        mod1 in 11..19 -> "тысяч"
        mod2 in 2..4 -> "тысячи"
        mod2 == 1 -> "тысяча"
        else -> "тысяч"
    }
}