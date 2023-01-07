import java.io.File

data class Point(val x: Int, val y: Int)

enum class Direction {
    NW,
    N,
    NE,
    W,
    E,
    SW,
    S,
    SE,
}

class Day23(path: String) {
    private val initialElves = mutableSetOf<Point>()

    init {
        var y = 0
        File(path).forEachLine { line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    initialElves.add(Point(x, y))
                }
            }
            y++
        }
    }

    private fun getSurrounding(p: Point, elves: Set<Point>): List<Boolean> {
        val surrounding = mutableListOf<Boolean>()

        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) {
                    continue
                }
                surrounding.add(Point(p.x + j, p.y + i) in elves)
            }
        }

        return surrounding.toList()
    }

    private fun getNextMove(elf: Point, elves: Set<Point>, start: Int): Point {
        val surrounding = getSurrounding(elf, elves)
        if (surrounding.count { it } == 0) {
            return elf
        }

        for (i in 0 until 4) {
            when ((i + start) % 4) {
                0 -> {
                    if (!surrounding[Direction.N.ordinal] && !surrounding[Direction.NE.ordinal] && !surrounding[Direction.NW.ordinal]) {
                        return Point(elf.x, elf.y - 1) // move north
                    }
                }

                1 -> {
                    if (!surrounding[Direction.S.ordinal] && !surrounding[Direction.SE.ordinal] && !surrounding[Direction.SW.ordinal]) {
                        return Point(elf.x, elf.y + 1) // move south
                    }
                }

                2 -> {
                    if (!surrounding[Direction.W.ordinal] && !surrounding[Direction.NW.ordinal] && !surrounding[Direction.SW.ordinal]) {
                        return Point(elf.x - 1, elf.y) // move west
                    }
                }

                3 -> {
                    if (!surrounding[Direction.E.ordinal] && !surrounding[Direction.NE.ordinal] && !surrounding[Direction.SE.ordinal]) {
                        return Point(elf.x + 1, elf.y) // move east
                    }
                }
            }
        }

        return elf
    }

    private fun groundTiles(elves: Set<Point>): Int {
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE

        elves.forEach {
            if (it.x < minX) {
                minX = it.x
            } else if (it.x > maxX) {
                maxX = it.x
            }

            if (it.y < minY) {
                minY = it.y
            } else if (it.y > maxY) {
                maxY = it.y
            }
        }

        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    fun part1(): Int {
        var elves = initialElves.toSet()
        var start = 0

        while (true) {
        //repeat (10) {
            // Determine next moves
            val nextCount = mutableMapOf<Point, Int>()
            val nextToPrev = mutableMapOf<Point, Point>()
            elves.forEach { elf ->
                val moveTo = getNextMove(elf, elves, start)
                if (moveTo in nextCount) {
                    nextCount[moveTo] = nextCount[moveTo]!! + 1
                } else {
                    nextCount[moveTo] = 1
                }
                nextToPrev[moveTo] = elf
            }

            // Move only the elves that need to move
            val nextElves = elves.toMutableSet()
            nextCount.forEach {
                if (it.value == 1) {
                    nextElves.remove(nextToPrev[it.key]!!)
                    nextElves.add(it.key)
                }
            }

            if (elves.intersect(nextElves).size == elves.size) {
                break
            }
            elves = nextElves
            start++
        }

        return start//groundTiles(elves)
    }
}

fun main() {
    val aoc = Day23("day23/input.txt")
    println(aoc.part1())
}
