import java.io.File

class Day22(input: String) {
    private val map: String
    private val path: String
    private var height = 0
    private var x = 0
    private var y = 0

    init {
        val file = File(input).readText().split("\n\n")
        map = file[0]
        path = file[1]
        height = map.split("\n").size
        x = map.indexOf('.')
    }

    fun part1(): Int {
        return 0
    }
}

fun main() {
    val aoc = Day22("day22/test1.txt")
    println(aoc.part1())
}
