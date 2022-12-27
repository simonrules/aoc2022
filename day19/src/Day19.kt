import java.io.File

class Day19(path: String) {
    data class Blueprint(
        val oreCostOre: Int,
        val clayCostOre: Int,
        val obsCostOre: Int,
        val obsCostClay: Int,
        val geoCostOre: Int,
        val geoCostObs: Int
    )

    enum class Robot(
        var collected: Int
    ) {
        ORE(0),
        CLAY(0),
        OBSIDIAN(0),
        GEODE(0)
    }

    private val blueprints = mutableListOf<Blueprint>()
    private val regex = "Blueprint \\d+: Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()

    init {
        File(path).forEachLine {
            val (a, b, c, d, e, f) = regex.matchEntire(it)!!.destructured
            blueprints.add(Blueprint(a.toInt(), b.toInt(), c.toInt(), d.toInt(), e.toInt(), f.toInt()))
        }
    }

    fun part1(): Int {
        return 0
    }
}

fun main() {
    val aoc = Day19("day19/test1.txt")
    println(aoc.part1())
}