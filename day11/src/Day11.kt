import java.io.File

enum class Operator {
    PLUS, MUL, SQUARE, UNKNOWN
}

data class Monkey(
    val id: Int,
    var inspectCount: Int,
    var items: MutableList<Long>,
    val operator: Operator,
    val operand: Int,
    val test: Int,
    val ifTrue: Int,
    val ifFalse: Int)

class Day11(path: String) {
    private val monkey = mutableListOf<Monkey>()

    init {
        val input = File(path).readText()
        val block = input.split("\n\n")
        var id = 0
        block.forEach {
            val line = it.split("\n")

            monkey.add(
                Monkey(
                    id++,
                    0,
                    getStarting(line[1]),
                    getOperation(line[2]),
                    getOperand(line[2]),
                    getTest(line[3]),
                    getTrue(line[4]),
                    getFalse(line[5])
                )
            )
        }
    }

    private fun getStarting(line: String): MutableList<Long> {
        val items = mutableListOf<Long>()

        val rhs = line.substring(18)
        val nums = rhs.split(", ")

        nums.forEach { items.add(it.toLong()) }

        return items
    }

    private fun getOperation(line: String): Operator {
        return when(line[23]) {
            '*' -> {
                if (line[25] == 'o') Operator.SQUARE else Operator.MUL
            }
            '+' -> Operator.PLUS
            else -> Operator.UNKNOWN
        }
    }

    private fun getOperand(line: String): Int {
        if (line[25] == 'o') {
            return 0
        }

        return line.substring(25).toInt()
    }

    private fun getTest(line: String): Int {
        return line.substring(21).toInt()
    }

    private fun getTrue(line: String): Int {
        return line.substring(29).toInt()
    }

    private fun getFalse(line: String): Int {
        return line.substring(30).toInt()
    }

    private fun applyOperation(worry: Long, operator: Operator, operand: Int): Long {
        return when(operator) {
            Operator.PLUS -> worry + operand
            Operator.MUL -> worry * operand
            Operator.SQUARE -> worry * worry
            else -> worry
        }
    }

    fun part1(): Int {
        for (r in 0 until 20) {
            monkey.forEach { m ->
                //println("Monkey ${m.id}:")
                val itemsToRemove = mutableSetOf<Int>()
                m.items.forEachIndexed { index, worry ->
                    //println("  Monkey inspects an item with a worry level of $worry")
                    var newWorry = applyOperation(worry, m.operator, m.operand)
                    m.inspectCount++
                    //println("    Worry level is ${m.operator} by ${m.operand} to $newWorry.")
                    newWorry /= 3L
                    //println("    Monkey gets bored with item. Worry level is divided by 3 to $newWorry.")
                    val throwTo = if (newWorry % m.test == 0L) m.ifTrue else m.ifFalse
                    //println("    Item with worry level $newWorry is thrown to monkey $throwTo.")
                    itemsToRemove.add(index)
                    monkey[throwTo].items.add(newWorry)
                }

                // Remove the thrown items
                val newItems = mutableListOf<Long>()
                m.items.forEachIndexed { index, worry ->
                    if (index !in itemsToRemove) {
                        newItems.add(worry)
                    }
                }
                m.items = newItems
            }
        }

        val inspections = monkey.map { it.inspectCount }.sortedDescending()

        return inspections[0] * inspections[1]
    }

    fun part2(): Long {
        val divisors = monkey.map { it.test }
        val product = divisors.reduce { acc, i -> acc * i }

        for (r in 0 until 10000) {
            monkey.forEach { m ->
                val itemsToRemove = mutableSetOf<Int>()
                m.items.forEachIndexed { index, worry ->
                    var newWorry = applyOperation(worry, m.operator, m.operand)
                    m.inspectCount++
                    newWorry %= product
                    val throwTo = if (newWorry % m.test == 0L) m.ifTrue else m.ifFalse
                    itemsToRemove.add(index)
                    monkey[throwTo].items.add(newWorry)
                }

                // Remove the thrown items
                val newItems = mutableListOf<Long>()
                m.items.forEachIndexed { index, worry ->
                    if (index !in itemsToRemove) {
                        newItems.add(worry)
                    }
                }
                m.items = newItems
            }
        }

        val inspections = monkey.map { it.inspectCount }.sortedDescending()

        return inspections[0].toLong() * inspections[1]
    }
}

fun main() {
    val aoc = Day11("day11/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}