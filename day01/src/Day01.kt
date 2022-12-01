import java.io.File

class Day01(path: String) {
    private val calories = mutableListOf<Int>()

    init {
        var food = 0
        File(path).forEachLine { line ->
            if (line.isEmpty()) {
                calories.add(food)
                food = 0
            } else {
                food += line.toInt()
            }
        }
        // Handle last line
        if (food > 0) {
            calories.add(food)
        }

        calories.sortDescending()
    }

    fun part1(): Int {
        return calories[0]
    }

    fun part2(): Int {
        return calories[0] + calories[1] + calories[2]
    }
}

fun main() {
    val aoc = Day01("day01/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}