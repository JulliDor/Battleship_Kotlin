fun checkHeight(iterator: Iterator<Int>) {
    iterator.forEach { if (it in 145..210) println("You can go!") else println("Sorry, not today")}
}

fun main() {
    val list = readln().split(" ").map(Integer::parseInt).toList()
    checkHeight(list.iterator())
}
