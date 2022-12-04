import java.io.File

fun IntRange.contains(other: IntRange): Boolean {
    return ((other.first >= first) && (other.last <= last))
}

private fun IntRange.overlap(other: IntRange): Boolean {
    return first.coerceAtLeast(other.first) <= last.coerceAtMost(other.last)
}

class Day04(path: String) {
    private val pairs = mutableListOf<Pair<IntRange, IntRange>>()

    init {
        File(path).forEachLine {
            val pair = it.split(",")
            val (a, b) = pair[0].split("-")
            val (c, d) = pair[1].split("-")

            pairs.add(Pair(IntRange(a.toInt(), b.toInt()), IntRange(c.toInt(), d.toInt())))
        }
    }

    fun part1(): Int {
        var count = 0
        pairs.forEach { pair ->
            val a = pair.first
            val b = pair.second

            if (a.contains(b) || b.contains(a)) {
                count++
            }
        }

        return count
    }

    fun part2(): Int {
        var count = 0
        pairs.forEach { pair ->
            val a = pair.first
            val b = pair.second

            if (a.overlap(b)) {
                count++
            }
        }

        return count
    }
}

fun main() {
    val aoc = Day04("day04/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}
