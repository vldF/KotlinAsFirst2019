package lesson7.task1

class Tag(tag: String, content: String) {
    var insertedTag: Tag? = null

    fun getText(): String = "<$this.tag>$this.content" + (this.insertedTag?.getText() ?: "") + "</$this.tag>"
}