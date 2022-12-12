import java.io.File
import java.util.*

data class Point(val x: Int, val y: Int)

data class Vertex(val point: Point, val dist: Int)

class Day12(path: String) {
    private val map = mutableListOf<Char>()
    private var dist = mutableListOf<Int>()
    private var height = 0
    private var width = 0
    private var start = Point(0, 0)
    private var end = Point(0, 0)

    // priority queue ordered by the dist ascending
    private val queue = PriorityQueue { v1: Vertex, v2: Vertex -> v1.dist - v2.dist }
    private val visited = mutableSetOf<Point>()

    init {
        var i = 0
        File(path).forEachLine { line ->
            width = line.length
            line.forEachIndexed { j, c ->
                when (c) {
                    'S' -> {
                        start = Point(j, i)
                        map.add('a')
                    }
                    'E' -> {
                        end = Point(j, i)
                        map.add('z')
                    } else -> map.add(c)
                }
            }
            i++
        }
        height = i
    }

    private fun getMapAt(p: Point): Char {
        return map[p.y * width + p.x]
    }

    private fun getDistAt(p: Point): Int {
        return dist[p.y * width + p.x]
    }

    private fun setDistAt(p: Point, v: Int) {
        dist[p.y * width + p.x] = v
    }

    private fun addToQueue(u: Point, v: Vertex) {
        // Always cost 1 to move to next cell
        val cost = 1

        if (!visited.contains(u)) {
            if (getDistAt(v.point) + cost < getDistAt(u)) {
                setDistAt(u, getDistAt(v.point) + cost)

                // update the dist, don't just insert a new element
                // we do this by removing and inserting
                queue.removeIf { it.point == u }
                queue.add(Vertex(u, getDistAt(u)))
            }
        }
    }

    private fun canVisit(there: Point, here: Point): Boolean {
        // any step down OK; at most 1 step up OK
        return getMapAt(there).code - 1 <= getMapAt(here).code
    }

    private fun reset() {
        queue.clear()
        visited.clear()
        dist = MutableList(map.size) { Int.MAX_VALUE }
    }

    private fun dijkstra(source: Point, dest: Point): Int {
        reset()
        setDistAt(source, 0) // distance to start is zero
        queue.add(Vertex(source, 0))

        while (queue.size > 0) {
            val v = queue.poll()
            // early termination; found dest
            if (v.point == dest) {
                return getDistAt(v.point)
            }
            visited.add(v.point)

            // consider four neighbouring nodes and visit all unvisited ones
            // left
            if (v.point.x > 0) {
                val there = Point(v.point.x - 1, v.point.y)
                if (canVisit(there, v.point)) {
                    addToQueue(there, v)
                }
            }

            // right
            if (v.point.x < width - 1) {
                val there = Point(v.point.x + 1, v.point.y)
                if (canVisit(there, v.point)) {
                    addToQueue(there, v)
                }
            }

            // top
            if (v.point.y > 0) {
                val there = Point(v.point.x, v.point.y - 1)
                if (canVisit(there, v.point)) {
                    addToQueue(there, v)
                }
            }

            // bottom
            if (v.point.y < height - 1) {
                val there = Point(v.point.x, v.point.y + 1)
                if (canVisit(there, v.point)) {
                    addToQueue(there, v)
                }
            }
        }

        return getDistAt(dest)
    }

    fun part1(): Int {
        return dijkstra(start, end)
    }

    fun part2(): Int {
        // Find all elevation a starting points
        val startPoints = mutableListOf<Point>()
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (getMapAt(Point(j, i)) == 'a') {
                    startPoints.add(Point(j, i))
                }
            }
        }

        // Find best starting point
        var min = Int.MAX_VALUE
        startPoints.forEach {
            val cost = dijkstra(it, end)
            if (cost < min) {
                min = cost
            }
        }

        return min
    }
}

fun main() {
    val aoc = Day12("day12/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
