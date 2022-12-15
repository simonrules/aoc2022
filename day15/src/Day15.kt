import java.io.File
import kotlin.math.abs
import kotlin.math.max

data class Point(val x: Int, val y: Int) {
    fun manhattan(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}

data class Sensor(val pos: Point, val beacon: Point, val coverage: Int)

class Day15(path: String) {
    private val regex =
        "Sensor at x=(-?\\d+), y=(=?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    private val sensors = mutableListOf<Sensor>()

    init {
        File(path).forEachLine {
            val (x1, y1, x2, y2) = regex.matchEntire(it)!!.destructured
            val s = Point(x1.toInt(), y1.toInt())
            val b = Point(x2.toInt(), y2.toInt())
            val coverage = s.manhattan(b)
            sensors.add(Sensor(s, b, coverage))
        }
    }

    private fun getRangeForRow(row: Int): List<IntRange> {
        // first get list of beacons affecting this row
        val affecting = mutableListOf<IntRange>()
        sensors.forEach {
            // find how many rows away the sensor is
            val dy = abs(it.pos.y - row)

            // check if the sensor could have a beacon on this row
            val rowCoverage = it.coverage - dy
            if (rowCoverage >= 0) {
                affecting.add(IntRange(it.pos.x - rowCoverage, it.pos.x + rowCoverage))
            }
        }

        // sort by first value
        val sorted = affecting.sortedBy { it.first }
        val coalesce = mutableListOf<IntRange>()

        // now coalesce
        coalesce.add(sorted[0])
        var cur = 0
        for (i in 1 until sorted.size) {
            if (sorted[i].first <= coalesce[cur].last) {
                // overlap
                coalesce[cur] = IntRange(coalesce[cur].first, max(coalesce[cur].last, sorted[i].last))
            } else {
                coalesce.add(sorted[i])
                cur++
            }
        }

        return coalesce
    }


    fun part1(): Int {
        val result = getRangeForRow(2000000)

        return result[0].last - result[0].first
    }

    fun part2(): Long {
        val range = 4000000

        for (y in 0 until range) {
            val result = getRangeForRow(y)

            if (result.size == 1) {
                // Must be at the very start or end
                if (result[0].first > 0) {
                    return y.toLong()
                } else if (result[0].last < range) {
                    return range.toLong() * range + y
                }
            } else {
                // There must be a single gap!
                val x = result[1].first + 1
                return x * range.toLong() + y
            }
        }

        return 0
    }
}

fun main() {
    val aoc = Day15("day15/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}