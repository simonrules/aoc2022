import java.io.File

data class Command(val num: Int, val from: Int, val to: Int)

class Day05(path: String) {
    private val stacks = mutableListOf<MutableList<Char>>()
    private val commands = mutableListOf<Command>()
    private val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

    init {
        var firstChunk = true

        File(path).forEachLine { line ->
            if (firstChunk) {
                if (line.isBlank()) {
                    firstChunk = false
                } else {
                    if (stacks.isEmpty()) {
                        // Add new stacks
                        for (i in 0..line.length step 4) {
                            stacks.add(mutableListOf())
                        }
                    }

                    for (i in stacks.indices) {
                        // Add crate
                        if (line[i * 4 + 1] in 'A'..'Z') {
                            stacks[i].add(line[i * 4 + 1])
                        }
                    }
                }
            } else {
                val match = regex.matchEntire(line)
                if (match != null) {
                    val (a, b, c) = match.destructured
                    commands.add(Command(a.toInt(), b.toInt() - 1, c.toInt() - 1))
                }
            }
        }
    }

    fun part1(): String {
        commands.forEach {
            for (i in 0 until it.num) {
                val crate = stacks[it.from].removeAt(0)
                stacks[it.to].add(0, crate)
            }
        }

        var first = ""
        stacks.forEach { first += it[0] }

        return first
    }

    fun part2(): String {
        commands.forEach {
            val pick = mutableListOf<Char>()
            for (i in 0 until it.num) {
                pick.add(stacks[it.from].removeAt(0))

            }
            stacks[it.to].addAll(0, pick)
        }

        var first = ""
        stacks.forEach { first += it[0] }

        return first
    }
}

fun main() {
    val aoc = Day05("day05/input.txt")

    //println(aoc.part1())
    println(aoc.part2())
}
