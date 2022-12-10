import java.io.File
import java.util.PriorityQueue

data class Write(val cycle: Int, val value: Int)

class Day10(path: String) {
    private val program = mutableListOf<Pair<String, Int?>>()
    // The priority queue acts as a write buffer
    private val writes = PriorityQueue { a: Write, b: Write -> a.cycle - b.cycle}

    init {
        File(path).forEachLine {
            val values = it.split(" ")
            val inst = values[0]
            val off = if (values.size == 2) values[1].toInt() else null
            program.add(Pair(inst, off))
        }
    }

    private fun drawPixel(cycle: Int, x: Int) {
        val crtPos = (cycle - 1) % 40
        if (crtPos in (x - 1..x + 1)) {
            print("#")
        } else {
            print(".")
        }
        if (crtPos == 39) {
            println()
        }
    }

    fun bothParts(): Int {
        var x = 1
        var cycle = 1
        val xPerCycle = mutableListOf<Int>()
        xPerCycle.add(0) // for zero indexing

        program.forEach { inst ->
            when (inst.first) {
                "addx" -> {
                    writes.add(Write(cycle + 2, inst.second!!))
                    xPerCycle.add(x)
                    xPerCycle.add(x)
                    drawPixel(cycle, x)
                    drawPixel(cycle + 1, x)
                    cycle += 2
                }
                else -> {
                    xPerCycle.add(x)
                    drawPixel(cycle, x)
                    cycle++
                }
            }

            while (writes.peek() != null && cycle >= writes.peek().cycle) {
                val write = writes.poll()
                x += write.value
            }
        }

        return xPerCycle[20] * 20 + xPerCycle[60] * 60 + xPerCycle[100] * 100 +
                xPerCycle[140] * 140 + xPerCycle[180] * 180 + xPerCycle[220] * 220
    }
}

fun main() {
    val aoc = Day10("day10/input.txt")

    println(aoc.bothParts())
}
