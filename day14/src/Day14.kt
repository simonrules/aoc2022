import java.io.File

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

class Day14(path: String) {
    // test1.txt  x: 494 -> 503 y: 4 -> 9
    // input.txt  x: 477 -> 544 y: 13 -> 167
    private val width = 1000
    private val height = 170
    private val source = Point(500, 0)
    private val map = Array(width * height) { '.' }

    private val down = Point(0, 1)
    private val left = Point(-1, 1)
    private val right = Point(1, 1)

    init {
        File(path).forEachLine { line ->
            val coords = line.split(" -> ")
            var prevPoint: Point? = null
            coords.forEach { coord ->
                val pair = coord.split(",")
                val point = Point(pair[0].toInt(), pair[1].toInt())
                if (prevPoint != null) {
                    if (point.x > prevPoint!!.x) {
                        for (x in prevPoint!!.x .. point.x) {
                            setMapAt(Point(x, point.y), '#')
                        }
                    } else if (point.x < prevPoint!!.x) {
                        for (x in point.x..prevPoint!!.x) {
                            setMapAt(Point(x, point.y), '#')
                        }
                    } else if (point.y > prevPoint!!.y) {
                        for (y in prevPoint!!.y..point.y) {
                            setMapAt(Point(point.x, y), '#')
                        }
                    } else {
                        for (y in point.y..prevPoint!!.y) {
                            setMapAt(Point(point.x, y), '#')
                        }
                    }
                }
                prevPoint = point
            }
        }
        setMapAt(source, '+')
    }

    private fun getMapAt(p: Point): Char {
        return map[p.y * width + p.x]
    }

    private fun setMapAt(p: Point, value: Char) {
        map[p.y * width + p.x] = value
    }

    private fun printMap(minX: Int, maxX: Int) {
        for (i in 0 until height) {
            for (j in minX until maxX) {
                print(getMapAt(Point(j, i)))
            }
            println()
        }
        println()
    }

    // Drop until it falls into abyss
    private fun dropSand(): Boolean {
        var s = source
        var canMove = true

        while (canMove) {
            val d = s + down
            val l = s + left
            val r = s + right
            if (getMapAt(d) == '.') {
                s = d
            } else if (getMapAt(l) == '.') {
                s = l
            } else if (getMapAt(r) == '.') {
                s = r
            } else {
                canMove = false
            }
            if (s.y >= height - 1) {
                return false
            }
        }
        setMapAt(s, 'o')

        return true
    }

    // Drop until source is blocked
    private fun dropSand2(): Boolean {
        var s = source
        var canMove = true

        while (canMove && getMapAt(source) != 'o') {
            val d = s + down
            val l = s + left
            val r = s + right
            if (getMapAt(d) == '.') {
                s = d
            } else if (getMapAt(l) == '.') {
                s = l
            } else if (getMapAt(r) == '.') {
                s = r
            } else {
                canMove = false
            }
        }
        setMapAt(s, 'o')

        return s != source
    }

    fun part1(): Int {
        var count = 0
        while (dropSand()) { count++ }

        return count
    }

    fun part2(): Int {
        for (x in 0 until width) {
            setMapAt(Point(x, height - 1), '#')
        }

        var count = 0
        while (dropSand2()) { count++ }

        return count + 1
    }
}

fun main() {
    val aoc = Day14("day14/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}
