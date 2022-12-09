import java.io.File
import kotlin.math.abs

data class Point2D(val x: Int, val y: Int) {
    operator fun minus(other: Point2D): Point2D {
        return Point2D(x - other.x, y - other.y)
    }

    fun farFrom(other: Point2D): Boolean {
        return (abs(x - other.x) > 1 || abs(y - other.y) > 1)
    }
}

class Day09(path: String) {
    val moves = mutableListOf<Pair<Char, Int>>()
    init {
        File(path).forEachLine {
            val (dir, value) = it.split(" ")
            moves.add(Pair(dir[0], value.toInt()))
        }
    }

    private fun moveHead(p: Point2D, move: Pair<Char, Int>): Point2D {
        return when (move.first) {
            'U' -> Point2D(p.x, p.y + 1)
            'D' -> Point2D(p.x, p.y - 1)
            'L' -> Point2D(p.x - 1, p.y)
            'R' -> Point2D(p.x + 1, p.y)
            else -> p
        }
    }

    private fun moveTail(h: Point2D, t: Point2D): Point2D {
        if (h.farFrom(t)) {
            // Find largest delta (x or y)
            var dx = h.x - t.x
            var dy = h.y - t.y
            if (abs(dx) > 1) {
                dx /= 2
            } else if (abs(dy) > 1) {
                dy /= 2
            }

            return Point2D(t.x + dx, t.y + dy)
        }

        return t
    }

    fun part1(): Int {
        val tailSet = mutableSetOf<Point2D>()

        var h = Point2D(0, 0)
        var t = Point2D(0, 0)
        moves.forEach {
            for (i in 0 until it.second) {
                h = moveHead(h, it)
                t = moveTail(h, t)
                tailSet.add(t)
            }
        }
        return tailSet.size
    }

    fun part2(): Int {

        return 0
    }
}

fun main() {
    val aoc = Day09("day09/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}
