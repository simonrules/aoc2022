import java.io.File

/*open class Monkey(val name: String)
class Number(name: String, value: Int) : Monkey(name)
class Operation(name: String, lhs: String, rhs: String)*/

data class Operation(val left: String, val operator: Char, val right: String)

class Day21(path: String) {
    private val values = mutableMapOf<String, Long>()
    private val operations = mutableMapOf<String, Operation>()

    init {
        File(path).forEachLine {
            val parts = it.split(": ")

            val monkey = parts[0]
            val value = parts[1].toLongOrNull()
            if (value == null) {
                val op = parts[1].split(" ")
                operations[monkey] = Operation(op[0], op[1][0], op[2])
            } else {
                values[monkey] = value
            }
        }
    }

    fun part1(): Long {
        while ("root" !in values) {
            val results = mutableListOf<Pair<String, Long>>()
            operations.forEach { (monkey, operation) ->
                val left = values[operation.left]
                val right = values[operation.right]
                if (left != null && right != null) {
                    val result = when (operation.operator) {
                        '+' -> left + right
                        '-' -> left - right
                        '*' -> left * right
                        '/' -> left / right
                        else -> 0
                    }
                    results.add(Pair(monkey, result))
                }
            }
            results.forEach {
                operations.remove(it.first)
                values[it.first] = it.second
            }
        }

        return values["root"]!!
    }

    fun part2(): Long {


        return 0L
    }
}

fun main() {
    val aoc = Day21("day21/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
