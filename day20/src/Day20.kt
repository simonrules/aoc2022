import java.io.File

class Day20(path: String) {
    private var list = mutableListOf<Long>()

    init {
        File(path).forEachLine { list.add(it.toLong()) }
    }

    fun part1(): Long {
        return solution(1L, 1)
    }

    fun part2(): Long {
        return solution(811589153L, 10)
    }

    private fun solution(key: Long, times: Int): Long {
        // using withIndex will store as (index, value) pairs, making each unique
        val unique = list.map { it * key }.withIndex().toMutableList()

        repeat(times) {
            list.indices.forEach { i ->
                val index = unique.indexOfFirst { it.index == i }
                val removed = unique.removeAt(index)
                // use mod instead of % to keep result positive
                unique.add((index + removed.value).mod(list.lastIndex), removed)
            }
        }

        val zeroIndex = unique.indexOfFirst { it.value == 0L }

        return listOf(1000, 2000, 3000).sumOf { unique[(zeroIndex + it) % list.size].value }
    }
}

fun main() {
    val aoc = Day20("day20/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
