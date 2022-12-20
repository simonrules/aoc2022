import java.io.File

data class Point3D(val x: Int, val y: Int, val z: Int)

class Day18(path: String) {
    private val points = mutableListOf<Point3D>()
    private var w = 0
    private var h = 0
    private var d = 0
    private var map = Array(0) { false }
    private var totalFaces = 0

    init {
        File(path).forEachLine {
            val (x, y, z) = it.split(",")
            points.add(Point3D(x.toInt() + 1, y.toInt() + 1, z.toInt() + 1)) // add buffer
            if (x.toInt() > w) {
                w = x.toInt()
            }
            if (y.toInt() > h) {
                h = y.toInt()
            }
            if (z.toInt() > d) {
                d = z.toInt()
            }
        }
        w += 3 // add buffer
        h += 3
        d += 3
        map = Array(w * h * d) { false }
    }

    private fun setAt(array: Array<Boolean>, p: Point3D) {
        array[w * h * p.z + w * p.y + p.x] = true
    }

    private fun getAt(array: Array<Boolean>, p: Point3D, outOfBounds: Boolean): Boolean {
        if (p.x < 0 || p.y < 0 || p.z < 0 || p.x >= w || p.y >= h || p.z >= d) {
            return outOfBounds
        }

        return array[w * h * p.z + w * p.y + p.x]
    }

    fun part1(): Int {
        var count = 0

        points.forEach { setAt(map, it) }

        for (i in 0 until d) {
            for (j in 0 until h) {
                for (k in 0 until w) {
                    if (getAt(map, Point3D(k, j, i), false)) {
                        count += if (getAt(map, Point3D(k - 1, j, i), false)) 0 else 1
                        count += if (getAt(map, Point3D(k + 1, j, i), false)) 0 else 1
                        count += if (getAt(map, Point3D(k, j - 1, i), false)) 0 else 1
                        count += if (getAt(map, Point3D(k, j + 1, i), false)) 0 else 1
                        count += if (getAt(map, Point3D(k, j, i - 1), false)) 0 else 1
                        count += if (getAt(map, Point3D(k, j, i + 1), false)) 0 else 1
                    }
                }
            }
        }

        /*for (z in 0 until d) {
            printSlice(map, z)
        }*/

        return count
    }

    private fun dfs(p: Point3D, visited: Array<Boolean>) {
        if (p.x < 0 || p.y < 0 || p.z < 0 || p.x >= w || p.y >= h || p.z >= d) {
            return
        }

        setAt(visited, p)

        var count = 0
        for (n in -1..1 step 2) {
            if (getAt(map, Point3D(p.x + n, p.y, p.z), false)) count++
            if (getAt(map, Point3D(p.x, p.y + n, p.z), false)) count++
            if (getAt(map, Point3D(p.x, p.y, p.z + n), false)) count++
        }
        totalFaces += count

        for (n in -1..1 step 2) {
            if (!getAt(visited, Point3D(p.x + n, p.y, p.z), true)) {
                dfs(Point3D(p.x + n, p.y, p.z), visited)
            }
            if (!getAt(visited, Point3D(p.x, p.y + n, p.z), true)) {
                dfs(Point3D(p.x, p.y + n, p.z), visited)
            }
            if (!getAt(visited, Point3D(p.x, p.y, p.z + n), true)) {
                dfs(Point3D(p.x, p.y, p.z + n), visited)
            }
        }
    }

    private fun bfs(root: Point3D, visited: Array<Boolean>) {
        val queue = mutableListOf<Point3D>()
        queue.add(root)
        setAt(visited, root)

        while (queue.isNotEmpty()) {
            val p = queue.removeAt(0)

            var count = 0
            for (n in -1..1 step 2) {
                if (getAt(map, Point3D(p.x + n, p.y, p.z), false)) count++
                if (getAt(map, Point3D(p.x, p.y + n, p.z), false)) count++
                if (getAt(map, Point3D(p.x, p.y, p.z + n), false)) count++
            }
            totalFaces += count

            for (n in -1..1 step 2) {
                val newX = Point3D(p.x + n, p.y, p.z)
                if (!getAt(visited, newX, true)) {
                    queue.add(newX)
                    setAt(visited, newX)
                }
                val newY = Point3D(p.x, p.y + n, p.z)
                if (!getAt(visited, newY, true)) {
                    queue.add(newY)
                    setAt(visited, newY)
                }
                val newZ = Point3D(p.x, p.y, p.z + n)
                if (!getAt(visited, newZ, true)) {
                    queue.add(newZ)
                    setAt(visited, newZ)
                }
            }
        }
    }

    private fun printSlice(array: Array<Boolean>, z: Int) {
        for (x in 0 until w) {
            for (y in 0 until h) {
                print(if (array[w * h * z + w * y + x]) '#' else '.')
            }
            println()
        }
        println()
    }

    fun part2(): Int {
        // Strategy: flood fill, locate cubes in the volume, mark new cubes as visited, then for those cubes
        // mark all faces that are touching the volume
        // need to increase the volume by 1 on all sides to account for "flow"
        // for some reason, dfs can't handle this measly amount of recursion...
        bfs(Point3D(0, 0, 0), map.clone())

        return totalFaces
    }
}

fun main() {
    val aoc = Day18("day18/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}