package battleship
import kotlin.math.absoluteValue

class SeaBattle {
    val seaField = MutableList(10) { MutableList(10) { "~" } }
    private val foggyField = MutableList(10) { MutableList(10) { "~" } }
    val listShips = mutableListOf<MutableList<String>>()

    fun battleshipGame(enemyField:MutableList<MutableList<String>>, numPlayer:Int) {
        var target:String
        while (true) {
            printField(foggyField).also{ println("---------------------") }.also { printField(enemyField) }.also { println("\nPlayer $numPlayer, it's your turn:") }
            target = readln()
            if (!target.matches(Regex("""[A-J](([1-9])|(10))"""))) println("\nError! You entered the wrong coordinates! Try again:") else break
        }
        val row = target[0] - 'A'
        val col = target.substring(1).toInt() - 1
        if (seaField[row][col] == "O") {
            foggyField[row][col] = "X".also { seaField[row][col] = "X"}
                if (killBill(row,col)) {
                    println(if (listShips.isNotEmpty()) "\nYou sank a ship!\nPress Enter and pass the move to another player\n...\n" else "\nYou sank the last ship. You won. Congratulations!")
                } else println("\nYou hit a ship!\nPress Enter and pass the move to another player\n...\n")
        } else foggyField[row][col] = "M".also{ seaField[row][col] = "M"}.also {  println("\nYou missed!\nPress Enter and pass the move to another player\n...\n") }
    }
    private fun killBill(row:Int, col:Int):Boolean {
        for (ship in listShips){
            for(c in ship.indices){
                if (ship[c] == "$row$col") ship[c] = "X"
            }
        }
        var j = -1
        listShips.forEachIndexed { i, it -> if(it.all { s -> s == "X"}) { j = i } }.also { if (j != -1) { listShips.removeAt(j); return true } }
        return false
    }
    fun createGameField() {
        printField(seaField)
        checkInputAndPut(5, "Aircraft Carrier").also { printField(seaField) }
        checkInputAndPut(4,"Battleship").also { printField(seaField) }
        checkInputAndPut(3,"Submarine").also { printField(seaField) }
        checkInputAndPut(3,"Cruiser").also { printField(seaField) }
        checkInputAndPut(2,"Destroyer").also { printField(seaField) }
        println("\nPress Enter and pass the move to another player\n...\n")
    }
    private fun checkInputAndPut(n: Int, name:String) {
        while (true) {
            println("\nEnter the coordinates of the $name ($n cells):\n")
            var input:String
            while(true) {
                input = readln()
                if(!input.matches(Regex("""[A-J](([1-9])|(10))\s[A-J](([1-9])|(10))"""))) println("\nError! You entered the wrong coordinates of the $name! Try again:\n")
                else break
            }
            val (start, end) = input.split(" ")
            val x1 = start[0].code - 65
            val x2 = end[0].code - 65
            val y1 = start.substring(1).toInt() - 1
            val y2 = end.substring(1).toInt() - 1
            when {
                x1 != x2 && y1 != y2 -> println("\nError! Wrong ship location! Try again:\n")
                (y2 - y1).absoluteValue + 1 != n && (x2 - x1).absoluteValue + 1 != n -> println("\nError! Wrong length of the $name! Try again:\n")
                y1 < y2 -> if (checkPosition(x1, y1, y2, "horizontal")) { putShip(x1, y1, y2,"horizontal"); return }
                y1 > y2 -> if (checkPosition(x1, y2, y1, "horizontal")) { putShip(x1, y2, y1,"horizontal"); return }
                x1 < x2 -> if (checkPosition(y1, x1, x2, "vertical")) { putShip(y1, x1, x2,"vertical"); return }
                x1 > x2 -> if (checkPosition(y1, x2, x1, "vertical")) { putShip(y1, x2, x1,"vertical"); return }
            }
        }
    }
    private fun checkPosition(x:Int, y:Int, z:Int, label:String):Boolean {
        for (i in y-1..z+1) {
            for(j in x-1..x+1){
                if (i<0 || i>9 || j<0 || j>9) continue
                if (label == "horizontal" && seaField[j][i] != "~" || label == "vertical" && seaField[i][j] != "~") { println("Error! You placed it too close to another one. Try again:"); return false  }
            }
        }
        return true
    }
    private fun putShip(x:Int, y:Int, z:Int, label:String){
        val shipsCoord = mutableListOf<String>()
        for(i in y..z){
            if (label == "horizontal") { seaField[x][i] = "O"; shipsCoord.add("$x$i") } else { seaField[i][x] = "O"; shipsCoord.add("$i$x") }
        }
        listShips.add(shipsCoord)
    }
}

fun printField(field:MutableList<MutableList<String>>) {
    println("  1 2 3 4 5 6 7 8 9 10")
    field.forEachIndexed { i, it -> println("${(i + 65).toChar()} ${it.joinToString(" ")}") }
}

fun main() {
    val player1 = SeaBattle().also { println("Player 1, place your ships on the game field\n") }.also { it.createGameField() }
    val player2 = SeaBattle().also { println("Player 2, place your ships on the game field\n") }.also { it.createGameField() }
    while(player1.listShips.isNotEmpty() && player2.listShips.isNotEmpty()) {
        player2.battleshipGame(player1.seaField, 1)
        player1.battleshipGame(player2.seaField,2)
    }
}
