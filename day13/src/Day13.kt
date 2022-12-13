import java.io.File

class Day13(path: String) {
    private val packet = mutableListOf<Pair<String, String>>()
    private val packetList = mutableListOf<String>()
    private val divider0 = "[[2]]"
    private val divider1 = "[[6]]"
    init {
        val blocks = File(path).readText().split("\n\n")
        blocks.forEach {
            val line = it.split("\n")
            packet.add(Pair(line[0], line[1]))
            packetList.add(line[0])
            packetList.add(line[1])
        }
        packetList.add(divider0)
        packetList.add(divider1)
    }

    private fun unNest(input: String): String {
        return input.substring(1, input.length - 1)
    }

    private fun compareLists(aList: List<String>, bList: List<String>): Int {
        if (aList.isEmpty() && bList.isNotEmpty()) {
            return -1 // in order
        }

        aList.forEachIndexed { i, a ->
            if (i >= bList.size) {
                // right list ran out of elements
                return 1 // out of order
            }
            val c = compareExpr(a, bList[i])
            if (c != 0) {
                return c
            }
        }

        if (aList.size < bList.size) {
            // left ran out of items
            return -1 // in order
        }

        return 0
    }

    private fun splitExpr(input: String): List<String> {
        if (input.isEmpty()) {
            return listOf()
        }

        val expr = StringBuilder(input)
        var level = 0
        for (i in expr.indices) {
            if ((expr[i] == ',') && (level == 0)) {
                expr[i] = '/'
            } else if (expr[i] == '[') {
                level++
            } else if (expr[i] == ']') {
                level--
            }
        }
        return expr.split("/")
    }

    private fun compareExpr(a: String, b: String): Int {
        val aIsList = a[0] == '['
        val bIsList = b[0] == '['

        if (!aIsList && !bIsList) {
            // Both numbers
            return a.toInt() - b.toInt()
        }

        val aList = if (aIsList) splitExpr(unNest(a)) else listOf(a)
        val bList = if (bIsList) splitExpr(unNest(b)) else listOf(b)

        return compareLists(aList, bList)
    }

    fun part1(): Int {
        var sum = 0
        packet.forEachIndexed { index, pair ->
            val c = compareExpr(pair.first, pair.second)
            if (c <= 0) {
                sum += (index + 1)
            }
        }

        return sum
    }

    fun part2(): Int {
        val sorted = packetList.sortedWith { a, b -> compareExpr(a, b) }

        return (sorted.indexOf(divider0) + 1) * (sorted.indexOf(divider1) + 1)
    }
}

fun main() {
    val aoc = Day13("day13/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}