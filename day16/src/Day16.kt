import java.io.File

data class Valve(val name: String, val flow: Int, val tunnel: Set<String>)

class Day16(path: String) {
    private val regex = "Valve (.+) has flow rate=(\\d+); tunnel.? lead.? to valve.? (.+)".toRegex()
    private val valves = mutableMapOf<String, Valve>()
    private var available = mutableListOf<String>()
    private val graph = mutableMapOf<Set<String>, Int>()
    private val starts = mutableSetOf<Pair<String, Int>>()
    private val paths = mutableListOf<List<String>>()
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

    private fun dfs(valve: String, timeLeft: Int, remaining: Set<String>, path: List<String>): Int {
        if (timeLeft <= 1) {
            return 0
        }

        var best = 0
        remaining.forEach { dest ->
            val dist = graph[setOf(valve, dest)]!!

            val newPressure = dfs(
                dest,
                timeLeft - dist - 1,
                remaining.minus(dest),
                path.plus(dest)
            )
            if (newPressure > best) {
                best = newPressure
            }
        }


        return valves[valve]!!.flow * (timeLeft - 1) + best
    }

    private fun dfs2(valveA: String, valveB: String, timeLeftA: Int, timeLeftB: Int, remaining: Set<String>): Int {
        if (timeLeftA < 1 && timeLeftB < 1) {
            return 0
        }

        var best = 0
        remaining.forEach { a ->
            remaining.forEach { b ->
                if (a != b) {
                    val distA = graph[setOf(valveA, a)]!!
                    val distB = graph[setOf(valveB, b)]!!

                    val newPressure = dfs2(
                            a,
                            b,
                            timeLeftA - distA - 1,
                            timeLeftB - distB - 1,
                            remaining.minus(a).minus(b)
                        )
                    if (newPressure > best) {
                        best = newPressure
                    }
                }
            }
        }

        return valves[valveA]!!.flow * (timeLeftA - 1) + valves[valveB]!!.flow * (timeLeftB - 1) + best
    }

    fun part1(): Int {
        val timeLeft = 30
        var best = 0
        starts.forEach {
            val pressure = dfs(
                it.first,
                timeLeft - it.second,
                available.toSet().minus(it.first),
                listOf(it.first)
            )
            if (pressure > best) {
                best = pressure
            }
        }

        return best
    }

    fun part2(): Int {
        val timeLeft = 26

        var best = 0
        starts.forEach { a ->
            starts.forEach { b ->
                if (a != b) {
                    val pressure = dfs2(
                        a.first,
                        b.first,
                        timeLeft - a.second,
                        timeLeft - b.second,
                        available.toSet().minus(a.first).minus(b.first)
                    )
                    if (pressure > best) {
                        best = pressure
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
    //2769 too low
    //println(aoc.part2())
}
