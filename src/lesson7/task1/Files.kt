@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import lesson3.task1.digitNumber
import lesson3.task1.revert
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.pow
import kotlin.math.sign

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val inpText = File(inputName).readText().toLowerCase()
    val res = mutableMapOf<String, Int>()
    for (subStr in substrings) {
        res[subStr] = strContainsStr(inpText, subStr.toLowerCase())
    }
    return res
}

fun strContainsStr(str: String, subStr: String) = str.windowed(subStr.length) { if (it == subStr) 1 else 0 }.sum()

/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val inp = File(inputName).readText()
    val res = StringBuilder()
    val chars = setOf('ж', 'ч', 'ш', 'щ')

    for (idx in inp.indices) {
        val toAdd = if (idx > 1 && inp[idx - 1].toLowerCase() in chars) {
            val oldLetter = inp[idx]
            val isUpper = oldLetter.isUpperCase()
            val letter = when (oldLetter.toLowerCase()) {
                'ы' -> 'и'
                'ю' -> 'у'
                'я' -> 'а'
                else -> oldLetter
            }
            if (isUpper) letter.toUpperCase() else letter
        } else inp[idx]
        res.append(toAdd)
    }
    File(outputName).writeText(res.toString())
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val inp = File(inputName).readLines()
    val maxLen = (inp.maxBy { it.length } ?: "").length
    val resText = StringBuilder()

    for (line in inp) {
        val l = line.trim()
        val spacesToAdd = (maxLen - l.length) / 2
        resText.append(" ".repeat(spacesToAdd)).append(l).append('\n')
    }
    File(outputName).writeText(resText.toString())
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val maxLen = (lines.maxBy { it.length } ?: "").length
    val resText = StringBuilder()

    for (lineRow in lines) {
        if (lineRow.isEmpty() || lineRow == "\n") {
            resText.append("\n")
            continue
        }
        val line = lineRow.trim().replace(Regex("\\s+"), " ")
        val splittedWords = line.split(" ")

        if (splittedWords.size == 1) {
            resText.append(splittedWords[0]).append("\n")
            continue
        }

        val charsDelta = max(0, maxLen - line.length - 2)
        val toOneSpace = charsDelta / (splittedWords.size - 1)  // -1 за то, что пробелов между n словами n-1
        var modSpaces = charsDelta % (splittedWords.size - 1)

        val resLine = StringBuilder()
        for (word in splittedWords.subList(0, splittedWords.size - 1)) {
            resLine.append(word).append(" ".repeat(toOneSpace + 1))
            if (modSpaces > 0) resLine.append(" ")
            modSpaces--
        }
        resLine.append(splittedWords.last())
        resText.append(resLine.append("\n").toString())
    }
    File(outputName).writeText(resText.toString())
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val inp = File(inputName).readText().toLowerCase()
    if (inp.isEmpty()) return mapOf()
    val words = Regex("[а-яa-zё]*[^,.\\s!?_\\-\\d]").findAll(inp).map { it.value }
    val untopped = mutableMapOf<String, Int>()

    for (word in words) {
        untopped[word] = untopped.getOrDefault(word, 0) + 1
    }
    val top = untopped.toSortedMap(compareBy<String> { untopped[it] }.thenBy { it })
    val res = mutableMapOf<String, Int>()
    for (key in top.keys) {
        if (res.size == 20) break

    }
    return mapOf()
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val text = File(inputName).readText()
    val res = StringBuilder()
    for (c in text) {
        val r = dictionary.getOrDefault(c.toLowerCase(), dictionary.getOrDefault(c.toUpperCase(), c.toString()))
            .toLowerCase()
        if (c.isUpperCase()) res.append(r.capitalize())
        else res.append(r)

    }
    File(outputName).writeText(res.toString())
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val inp = File(inputName).readLines()
    var maxLen = 0
    val res = mutableListOf<String>()

    for (line in inp) {
        if (hasDifferentLetters(line)) {
            if (line.length > maxLen) {
                res.clear()
                maxLen = line.length
            } else if (line.length < maxLen) {
                continue
            }
            res.add(line)
        }
    }

    File(outputName).writeText(res.joinToString(separator = ", "))
}

fun hasDifferentLetters(str: String): Boolean {
    val letters = str.toLowerCase().toSet()
    return (letters.size == str.length)
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val res = StringBuilder("<html><body>")
    val originalText = File(inputName).readLines()
    var paragraphBuffer = StringBuffer()

    for (line in originalText) {
        if (line == "\n" || line == "") {
            res.append(paragraphBuffer).append("</p>")
            paragraphBuffer = StringBuffer()
            continue
        }
        if (paragraphBuffer.isEmpty()) paragraphBuffer.append("<p>")
        var sTmp = replaceCharsToTag(line, "**", "<b>", "</b>")
        sTmp = replaceCharsToTag(sTmp, "*", "<i>", "</i>")
        sTmp = replaceCharsToTag(sTmp, "~~", "<s>", "</s>")
        paragraphBuffer.append(sTmp)
    }
    if (paragraphBuffer.isNotBlank()) {
        res.append(paragraphBuffer).append("</p>")
    }
    res.append("</body></html>")
    File(outputName).writeText(res.toString())
}

fun replaceCharsToTag(str: String, s: String, openTag: String, closeTag: String): String {
    var idx = 0
    val result = StringBuilder()
    var nowOpen = true
    while (idx < str.length) {
        val sliced = str.slice(idx..min(idx + s.length - 1, str.length - 1))
        if (sliced == s) {
            result.append(if (nowOpen) openTag else closeTag)
            nowOpen = !nowOpen
            idx += s.length - 1
        } else {
            result.append(str[idx])
        }
        idx++
    }
    return result.toString()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>
Или колбаса
</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>
Фрукты
<ol>
<li>Бананы</li>
<li>
Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    // Не доделал и нт идей, как доделать
    val inp = File(inputName).readLines()
    val olIdxRegex = "^\\s*(\\d+)\\.\\s".toRegex() // todo remember to use 1 group
    val isUlRegex = "^\\s*[*]".toRegex()
    val olContentRegex = "^\\s*\\d+\\.\\s(.*)".toRegex()
    val ulContentRegex = "^\\s*\\*\\s(.+)".toRegex() // todo 1 group
    val res = Node("html", StringBuilder())
    res.insertedTags.add(Node("body", StringBuilder()))

    for (lineIdx in inp.indices) {
        val line = inp[lineIdx]
        val currentSpaces = getSpaces(line)
        val tag = when {
            isUlRegex.find(line) != null -> "ul"
            olIdxRegex.find(line)?.groupValues != null -> "ol"
            else -> ""
        }
        val content = when (tag) {
            "ol" -> olContentRegex.find(line)!!.value
            else -> ulContentRegex.find(line)?.groupValues?.get(1) ?: ""
        }
        val lvl = res.getNodeByLvl(currentSpaces / 4 + 2)
        if (tag.isNotEmpty()) {
            val prevSpaces = if (lineIdx > 0) getSpaces(inp[lineIdx - 1]) else -1
            var pointer: Node
            pointer = when {
                prevSpaces == -1 -> lvl.add(Node(tag))
                prevSpaces < currentSpaces -> lvl.add(Node(tag))
                prevSpaces == currentSpaces -> lvl.insertedTags.last()
                else -> res.getNodeByLvl(currentSpaces / 4 + 1)
            }
            pointer.add(Node("li", StringBuilder(content)))
        } else {
            lvl.content.append(line)
        }
    }
    File(outputName).writeText(res.getText())
}

fun getSpaces(str: String): Int = "^\\s*".toRegex().find(str)?.value?.length ?: 0

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val res = mutableListOf<String>()
    val nums = mutableListOf<Int>()

    var n = rhv
    val ans = lhv * rhv
    while (n > 0) {
        nums.add(lhv * (n % 10))
        n /= 10
    }

    val maxLen = digitNumber(nums.last()) + nums.size
    res.add(" $ans")
    res.add("-".repeat(maxLen))
    var first = false
    var nextLen = 1 + digitNumber(nums.last())

    for ((spaces, num) in nums.reversed().withIndex()) {
        if (spaces == nums.size - 1) first = true
        if (first) {
            res.add(" ".repeat(nextLen - digitNumber(num)) + "$num")
        } else {
            res.add("+" + " ".repeat(nextLen - digitNumber(num) - 1) + "$num")
        }
        nextLen++
    }
    res.add("-".repeat(maxLen))
    res.add("*" + " ".repeat(maxLen - digitNumber(rhv) - 1) + "$rhv")
    res.add(" ".repeat(maxLen - digitNumber(lhv)) + "$lhv")
    File(outputName).writeText(res.reversed().joinToString(separator = "\n"))
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}

