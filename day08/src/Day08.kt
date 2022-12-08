import java.io.File

class Day08(path: String) {
    private val map = mutableListOf<Int>()
    private var height = 0
    private var width = 0

    init {
        var i = 0
        var j = 0
        File(path).forEachLine { line ->
            j = line.length
            line.forEach {
                map.add(Character.getNumericValue(it))
            }
            i++
        }
        height = i
        width = j
    }

    private fun getMapAt(x: Int, y: Int): Int {
        return map[y * width + x]
    }

    private fun isVisible(x: Int, y: Int): Boolean {
        val h = getMapAt(x, y)

        // up
        var taller = true
        for (i in 0 until y) {
            if (h <= getMapAt(x, i)) {
                taller = false
            }
        }
        if (taller) {
            return true
        }

        // down
        taller = true
        for (i in y + 1 until height) {
            if (h <= getMapAt(x, i)) {
                taller = false
            }
        }
        if (taller) {
            return true
        }

        // left
        taller = true
        for (j in 0 until x) {
            if (h <= getMapAt(j, y)) {
                taller = false
            }
        }
        if (taller) {
            return true
        }

        // right
        taller = true
        for (j in x + 1 until width) {
            if (h <= getMapAt(j, y)) {
                taller = false
            }
        }
        if (taller) {
            return true
        }

        return false
    }

    private fun scenicScore(x: Int, y: Int): Int {
        val h = getMapAt(x, y)

        // up
        var count0 = 0
        for (i in y - 1 downTo 0) {
            count0++
            if (h <= getMapAt(x, i)) {
                break
            }
        }

        // down
        var count1 = 0
        for (i in y + 1 until height) {
            count1++
            if (h <= getMapAt(x, i)) {
                break
            }
        }

        // left
        var count2 = 0
        for (j in x - 1 downTo 0) {
            count2++
            if (h <= getMapAt(j, y)) {
                break
            }
        }

        // right
        var count3 = 0
        for (j in x + 1 until width) {
            count3++
            if (h <= getMapAt(j, y)) {
                break
            }
        }

        return count0 * count1 * count2 * count3
    }

    fun part1(): Int {
        var count = 0
        for (i in 1 until height - 1) {
            for (j in 1 until width - 1) {
                if (isVisible(j, i)) {
                    count++
                }
            }
        }

        return count + (width * 2) + (height * 2) - 4
    }

    fun part2(): Int {
        var max = 0
        for (i in 0 until height) {
            for (j in 0 until width) {
                val scenic = scenicScore(j, i)
                if (scenic > max) {
                    max = scenic
                }
            }
        }

        return max
    }
}

fun main() {
    val aoc = Day08("day08/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}