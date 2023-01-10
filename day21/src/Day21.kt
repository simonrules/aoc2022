import java.io.File

data class Operation(val left: String, val operator: Char, val right: String)

class Day21(path: String) {
    private val initialValues: Map<String, Long>
    private val initialOperations: Map<String, Operation>

    init {
        val values = mutableMapOf<String, Long>()
        val operations = mutableMapOf<String, Operation>()

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

        initialValues = values.toMap()
        initialOperations = operations.toMap()
    }

    private fun sumTree(root: String, humn: Long? = null): Long {
        val values = initialValues.toMutableMap()
        val operations = initialOperations.toMutableMap()

        if (humn != null) {
            values["humn"] = humn
        }

        while (root !in values) {
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

        return values[root]!!
    }

    private fun findParent(child: String): String? {
        initialOperations.forEach {
            if (it.value.left == child || it.value.right == child) {
                return it.key
            }
        }

        return null
    }

    private fun isChildOf(root: String, child: String): Boolean {
        var current: String? = child
        do {
            current = findParent(current!!)
        } while (current != null && current != root)

        return current == root
    }

    fun part1(): Long {
        return sumTree("root")
    }

    fun part2(): Long {
        // Determine which side of root has "humn"
        val left = initialOperations["root"]!!.left
        val right = initialOperations["root"]!!.right
        val humnTree = if (isChildOf(right, "humn")) right else left
        val otherTree = if (isChildOf(right, "humn")) left else right

        // Make other side equal to needed by changing value of humn
        val needed = sumTree(otherTree)

        // Make two guesses to determine the direction to try
        val guess1 = 0L
        val result1 = sumTree(humnTree, guess1)
        val guess2 = 1000L
        val result2 = sumTree(humnTree, guess2)

        val direction = if (result2 > result1) -1 else 1
        var error = result2 - needed

        var guess = guess2
        while (error != 0L) {
            guess += direction * error / 10L
            val result = sumTree(humnTree, guess)
            error = result - needed
        }

        return guess
    }
}

fun main() {
    val aoc = Day21("day21/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
