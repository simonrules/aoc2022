import java.io.File

class Day02(path: String) {
    private val rounds = mutableListOf<Pair<Char, Char>>()
    private val values = mapOf('X' to 1, 'Y' to 2, 'Z' to 3, 'A' to 1, 'B' to 2, 'C' to 3)
    private val toWin = mapOf('A' to 'Y', 'B' to 'Z', 'C' to 'X')
    private val toLose = mapOf('A' to 'Z', 'B' to 'X', 'C' to 'Y')
    private val win = mapOf('X' to 'C', 'Y' to 'A', 'Z' to 'B')
    private val draw = mapOf('X' to 'A', 'Y' to 'B', 'Z' to 'C')

    init {
        File(path).forEachLine { line ->
            val round = line.split(" ")
            rounds.add(Pair(round[0][0], round[1][0]))
        }
    }

    fun part1(): Int {
        var score = 0
        rounds.forEach {
            score += values[it.second]!!

            if (win[it.second]!! == it.first) {
                score += 6
            } else if (draw[it.second]!! == it.first) {
                score += 3
            }
        }

        return score
    }

    fun part2(): Int {
        var score = 0
        rounds.forEach {
            score += when (it.second) {
                'Y' -> {
                    // draw
                    values[it.first]!! + 3
                }
                'Z' -> {
                    // win
                    values[toWin[it.first]!!]!! + 6
                }
                else -> {
                    // lose
                    values[toLose[it.first]!!]!!
                }
            }
        }

        return score
    }
}

fun main() {
    val aoc = Day02("day02/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}
