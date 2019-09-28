package lesson7.task1

class Node(_tag: String, _content: StringBuilder = StringBuilder()) {
    var insertedTags = mutableListOf<Node>()
    val content = _content
    val tag = _tag

    fun getText(): String = "<$tag>$content${insertedTags.joinToString(separator = "") { it.getText() }}</$tag>"

    fun getNodeByLvl(lvl: Int): Node {
        if (lvl == 0) return this
        return insertedTags.last().getNodeByLvl(lvl - 1)
    }

    fun add(node: Node): Node {
        insertedTags.add(node)
        return node
    }
}