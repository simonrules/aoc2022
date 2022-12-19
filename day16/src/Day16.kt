import java.io.File

data class Valve(val name: String, val flow: Int, val tunnel: Set<String>)

class Day16(path: String) {
    private val regex = "Valve (.+) has flow rate=(\\d+); tunnel.? lead.? to valve.? (.+)".toRegex()
    private val valves = mutableMapOf<String, Valve>()
    private var available = mutableListOf<String>()
    private val graph = mutableMapOf<Set<String>, Int>()
    private val starts = mutableSetOf<Pair<String, Int>>()
    private val paths = mutableListOf<Int>()
    private val pressures = mutableListOf<Int>()

    init {
        File(path).forEachLine {
            val (a, b, c) = regex.matchEntire(it)!!.destructured
            val tunnel = c.split(", ").toSet()
            valves[a] = Valve(a, b.toInt(), tunnel)
            if (valves[a]!!.flow > 0) {
                available.add(a)
            }
        }

        // simplify graph: how long does it take to get from one functional valve to another?
        for (i in available.indices) {
            //for (j in i + 1 until available.size) {
            for (j in available.indices) {
                if (i == j) {
                    continue
                }
                val a = available[i]
                val b = available[j]
                val dist = bfs(a, b)
                //println("from $a to $b is $dist")
                graph[setOf(a, b)] = dist
            }
        }

        // find starting points
        available.forEach { dest ->
            val dist = bfs("AA", dest)
            //println("from AA to $dest is $dist")
            starts.add(Pair(dest, dist))
        }
    }

    data class Vertex(val name: String, val dist: Int)
    private fun bfs(start: String, end: String): Int {
        val queue = mutableListOf<Vertex>()
        val visited = mutableSetOf<String>()

        val root = Vertex(start, 0)

        visited.add(root.name)
        queue.add(0, root)

        while (queue.isNotEmpty()) {
            val v = queue.removeAt(0)
            if (v.name == end) {
                return v.dist
            }

            valves[v.name]!!.tunnel.forEach {
                if (it !in visited) {
                    visited.add(it)
                    val vertex = Vertex(it, v.dist + 1)
                    queue.add(vertex)
                }
            }
        }

        println("oops")
        return 0
    }

    private fun dfs(pressure: Int, valve: String, timeLeft: Int, remaining: Set<String>, path: Int) {
        val newPressure = pressure + valves[valve]!!.flow * (timeLeft - 1)
        val newPath = addToPath(path, valve)

        if (remaining.isEmpty()) {
            paths.add(newPath)
            pressures.add(newPressure)
            return
        }

        remaining.forEach { dest ->
            val dist = graph[setOf(valve, dest)]!!
            if (dist < timeLeft) {
                dfs(
                    newPressure,
                    dest,
                    timeLeft - dist - 1,
                    remaining.minus(dest),
                    newPath
                )
            } else {
                paths.add(newPath)
                pressures.add(newPressure)
            }
        }
    }

    fun part1(): Int {
        val timeLeft = 30
        starts.forEach {
            dfs(
                0,
                it.first,
                timeLeft - it.second,
                available.toSet().minus(it.first),
                0
            )
        }

        val sorted = pressures.sortedDescending()

        return sorted[0]
    }

    private fun addToPath(path: Int, valve: String): Int {
        val bit = available.indexOf(valve)
        return path or (1 shl bit)
    }

    fun part2(): Int {
        val timeLeft = 26
        starts.forEach {
            dfs(
                0,
                it.first,
                timeLeft - it.second,
                available.toSet().minus(it.first),
                0
            )
        }

        // reduce the candidate space. 1100 is arbitrary.
        val newPressures = mutableListOf<Int>()
        val newPaths = mutableListOf<Int>()
        pressures.forEachIndexed { index, pressure ->
            if (pressure >= 1100) {
                newPressures.add(pressure)
                newPaths.add(paths[index])
            }
        }

        var best = 0
        for (a in 0 until newPressures.size) {
            for (b in a + 1 until newPressures.size) {
                val totalPressure = newPressures[a] + newPressures[b]

                // must be greater than the part 1 result
                if (totalPressure > 2124) {
                    if (newPaths[a] and newPaths[b] == 0) {
                        // no valves overlap
                        if (totalPressure > best) {
                            best = totalPressure
                        }
                    }
                }
            }
        }

        return best
    }
}

fun main() {
    val aoc = Day16("day16/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
