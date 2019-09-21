@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.IllegalArgumentException

// Месяцы для функций
val months = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря"
)

/**
 * Функция, которая проверяет, что строка на входе - валидное число
 */
fun isNumber(str: String): Boolean {
    for (c in str) if (!c.isDigit()) return false
    return true
}

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val parts = str.split(" ")
    if (parts.size != 3) return ""
    if (!isNumber(parts[0]) || !isNumber(parts[2])) return ""
    val day = parts[0].toInt()
    val month = months.indexOf(parts[1]) + 1
    val year = parts[2].toInt()

    if (month !in 1..12 || daysInMonth(month, year) < day) return ""
    return String.format("%02d.%02d.%d", day, month, year)
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val parts = digital.split(".")
    if (parts.size != 3 || !isNumber(parts[1]) || !isNumber(parts[2])) return ""
    val day = parts[0].toInt()
    val monthNumber = parts[1].toInt()
    if (monthNumber !in 1..12) return ""
    val month = months[monthNumber - 1]
    val year = parts[2].toInt()

    if (daysInMonth(monthNumber, year) < day) return ""
    return String.format("%d %s %d", day, month, year)
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    val validChars = setOf('(', ')', '-', '+', ' ')
    var isParentheses = false
    var hasParentheses = false
    var validParentheses = false
    val res = StringBuilder()
    for (c in phone) {
        if (c == '+' || c.isDigit()) res.append(c)
        if (c == '(') {
            if (isParentheses || hasParentheses) return ""
            isParentheses = true
            hasParentheses = true
        }
        if (c == ')') {
            if (!isParentheses || !validParentheses) return ""
            isParentheses = false
        }
        if (isParentheses && c.isDigit()) validParentheses = true
        if (!validChars.contains(c) && !c.isDigit()) return ""
    }

    return if (isParentheses) "" else res.toString()
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.isBlank()) return -1
    var hasNum = false
    for (c in jumps) {
        if (c != '-' && c != ' ' && c != '%' && !c.isDigit()) return -1
        if (c.isDigit()) hasNum = true
    }
    if (!hasNum) return -1
    return jumps.split(" ").filter { it[0].isDigit() }.map { it.toInt() }.max() ?: -1
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val attempts = jumps.split(' ')
    var max = -1
    for (i in 0 until attempts.size / 2) {
        val attemptVal = attempts[i * 2].toInt()
        val resultOfAttempt = attempts[i * 2 + 1]
        if (attemptVal > max && resultOfAttempt.contains('+')) max = attemptVal
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    require(expression.isNotEmpty())
    val tokens = expression.split(" ")
    var sum = if (isNumber(tokens[0])) tokens[0].toInt() else throw IllegalArgumentException()
    var isLastOperator = false
    var isLastOperatorPlus = false
    for (token in tokens.slice(1 until tokens.size)) {
        if (!isNumber(token) && token.length == 1) {
            require(!isLastOperator)
            isLastOperatorPlus = token == "+"
            isLastOperator = true
        } else {
            require(!(token[0] == '+' || token[0] == '-') && isLastOperator)
            isLastOperator = false
            val value = token.toInt()
            sum += if (isLastOperatorPlus) value else -value
        }
    }
    return sum
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    var lastWord = ""
    var currentSum = -1
    for (word in str.split(" ")) {
        if (lastWord == word.toLowerCase()) return currentSum - word.length
        lastWord = word.toLowerCase()
        currentSum += word.length + 1 // +1 так как пробелы не считаются
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    var maxName = ""
    var maxValue = -1.0
    for (itemPair in description.split("; ")) {
        val splitted = itemPair.split(" ")
        if (splitted.size != 2 || itemPair.contains(";")) return ""
        val name = splitted[0]
        val value = splitted[1].toDouble()

        if (value > maxValue) {
            maxValue = value
            maxName = name
        }
    }
    return if (maxName.isNotEmpty()) maxName else ""
}

fun maxCharsInRepite(str: String): Int {
    var currentChar = ' '
    var currentCount = 0
    var maxRep = 0
    for (i in str) {
        when {
            i == currentChar -> currentCount++
            currentCount > maxRep -> maxRep = currentCount
            else -> {
                currentChar = i
                currentCount = 0
            }
        }
    }
    return maxRep
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    val romeNumbers = mapOf('M' to 1000, 'D' to 500, 'C' to 100, 'L' to 50, 'X' to 10, 'V' to 5, 'I' to 1)
    if (roman.isEmpty() || maxCharsInRepite(roman) > 3 || !romeNumbers.keys.contains(roman.last())) return -1

    var res = 0
    for (i in 0 until roman.length - 1) {
        if (!romeNumbers.keys.contains(roman[i])) return -1
        if (!romeNumbers.keys.contains(roman[i + 1])) return -1
        if (romeNumbers.keys.indexOf(roman[i]) > romeNumbers.keys.indexOf(roman[i + 1])) {
            res -= romeNumbers.getValue(roman[i])
        } else res += romeNumbers.getValue(roman[i])
    }
    res += romeNumbers.getValue(roman.last())
    return res
}

/**
 * Возвращает следующую закрытую квадратную скобку (индекс), которая относится к [ или -1
 */
fun getNextCloseBracketOrError(str: String): Int {
    var bracketCount = 0
    var idx = 0
    for (c in str) {
        if (c == '[') bracketCount++
        else if (c == ']') bracketCount--
        if (bracketCount == 0) return idx
        idx++
    }
    return -1
}

/**
 * Возвращает предыдущую открытую квадратную скобку (индекс), которая относится к ] или -1
 */
fun getNextOpenBracketOrError(str: String): Int {
    var bracketCount = 0
    var idx = 0
    for (c in str.reversed()) {
        if (c == ']') bracketCount++
        else if (c == '[') bracketCount--
        if (bracketCount == 0) return idx
        idx++
    }
    return -1
}

fun validateBracket(str: String): Boolean {
    var bracketCount = 0
    var idx = 0
    for (c in str) {
        if (c == '[') bracketCount++
        else if (c == ']') bracketCount--
        idx++
        if (bracketCount < 0) return false
    }
    return bracketCount == 0
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    var clock = 0
    var pos = cells / 2
    var commandsSteps = 0
    val line = MutableList(cells) { 0 }
    require(validateBracket(commands)) { "Ошибка со скобками" }
    while (clock < limit && commandsSteps < commands.length) {
        check(pos in 0 until cells)
        when (commands[commandsSteps]) {
            ' ' -> {
                //do nothing todo: Как сделать этот кусок кода лучше? Как не делать ничего?
            }
            '+' -> line[pos]++
            '-' -> line[pos]--
            '>' -> pos++
            '<' -> pos--
            '[' -> {
                val idx = getNextCloseBracketOrError(commands.slice(commandsSteps until commands.length))
                require(idx != -1)
                if (line[pos] == 0) {
                    commandsSteps += idx
                }
            }
            ']' -> {
                val idx = getNextOpenBracketOrError(commands.slice(0..commandsSteps))
                require(idx != -1)
                if (line[pos] != 0) {
                    commandsSteps -= idx
                }
            }
            else -> throw IllegalArgumentException("Неверный символ")
        }
        clock++
        commandsSteps++
    }
    require(pos in 0 until cells)
    return line
}
