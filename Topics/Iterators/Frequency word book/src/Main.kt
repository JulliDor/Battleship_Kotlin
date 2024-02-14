fun main() {
    var list = readln().split(" ").groupingBy { it }.eachCount()
    var iterator = list.iterator()
    iterator.forEach { (k, v) -> println("$k $v") }   
}
