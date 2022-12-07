import java.io.File

data class Node(val name: String, var size: Int, val isDir: Boolean, val children: MutableList<Node>)

class Day07(path: String) {
    private val input = mutableListOf<String>()
    private val dirSize = mutableListOf<Int>()

    private var root: Node
    init {
        File(path).forEachLine {
            input.add(it)
        }

        root = Node("/", 0, true, mutableListOf())
        parseTree(1, root)
    }

    private fun parseTree(pos: Int, node: Node): Int {
        var i = pos
        while (i < input.size - 1) {
            val line = input[i]
            i++

            if (line[0] == '$') {
                val command = line.split(" ")
                if (command[1] == "cd") {
                    if (command[2] == "..") {
                        dirSize.add(node.size)
                        return i
                    }
                    val newNode = Node(command[2], 0, true, mutableListOf())
                    node.children.add(newNode)
                    i = parseTree(i, newNode)
                    node.size += newNode.size
                } else if (command[1] == "ls") {
                    while (i < input.size && input[i][0] != '$') {
                        val file = input[i].split(" ")
                        i++
                        var totalSize = 0

                        if (file[0] != "dir") {
                            totalSize += file[0].toInt()
                            val child = Node(file[1], file[0].toInt(), false, mutableListOf())
                            node.size += totalSize
                            node.children.add(child)
                        }
                    }
                }
            }
        }
        return i
    }

    fun part1(): Int {
        return dirSize.sumOf { if (it < 100000) it else 0 }
    }

    fun part2(): Int {
        val needed = 30000000 - (70000000 - root.size)

        val sizes = dirSize.sorted()

        sizes.forEach {
            if (it > needed) {
                return it
            }
        }

        return 0
    }
}

fun main() {
    val aoc = Day07("day07/input.txt")

    println(aoc.part1())
    println(aoc.part2())
}