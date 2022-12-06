import java.io.File

class Day06(path: String) {
    private var input: String

    init {
        input = File(path).readText()
    }

    private fun findMarker(window: Int): Int {
        for (i in 0 until input.length - window - 1) {
            val sub = input.subSequence(i, i + window)
            if (sub.toSet().size == window) {
                return i + window
            }
        }

        return -1
    }

    fun part1(): Int {
        val window = 4

        return findMarker(window)
    }

    fun part2(): Int {
        val window = 14

        return findMarker(window)
    }
}

fun main() {
    val aoc = Day06("day06/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}
