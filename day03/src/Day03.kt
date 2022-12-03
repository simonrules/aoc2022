import java.io.File

class Day03(path: String) {
    private val sack = mutableListOf<String>()

    init {
        File(path).forEachLine {
            sack.add(it)
        }
    }

    private fun priority(type: Char): Int {
        return when(type) {
            in 'a'..'z' -> type.code - 96
            in 'A'..'Z' -> type.code - 38
            else -> 0
        }
    }

    fun part1(): Int {
        var sum = 0
        sack.forEach {
            val len = it.length / 2
            val a = it.take(len).toSet()
            val b = it.substring(len).toSet()
            val intersect = a.intersect(b)
            sum += priority(intersect.first())
        }

        return sum
    }

    fun part2(): Int {
        var sum = 0
        for (i in 0 until sack.size step 3) {
            val a = sack[i].toSet()
            val b = sack[i + 1].toSet()
            val c = sack[i + 2].toSet()
            val intersect = a.intersect(b.intersect(c))
            sum += priority(intersect.first())
        }

        return sum
    }
}

fun main() {
    val aoc = Day03("day03/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}