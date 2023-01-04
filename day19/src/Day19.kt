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

    data class State(
        val minute: Int,
        val oreRobots: Int,
        val ore: Int,
        val clayRobots: Int,
        val clay: Int,
        val obsRobots: Int,
        val obs: Int,
        val geoRobots: Int,
        val geo: Int
    )

    private val blueprints = mutableListOf<Blueprint>()
    private val regex = "Blueprint \\d+: Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()

    init {
        File(path).forEachLine {
            val (a, b, c, d, e, f) = regex.matchEntire(it)!!.destructured
            blueprints.add(Blueprint(a.toInt(), b.toInt(), c.toInt(), d.toInt(), e.toInt(), f.toInt()))
        }
    }

    private fun bfs(root: State, b: Blueprint, maxMinutes: Int): Int {
        val queue = ArrayDeque<State>()
        val visited = hashSetOf<State>()
        val maxOreRobotsNeeded = listOf(b.oreCostOre, b.clayCostOre, b.obsCostOre, b.geoCostOre).max()
        val maxClayRobotsNeeded = b.obsCostClay
        val maxObsRobotsNeeded = b.geoCostObs
        var maxGeo = 0
        queue.add(root)

        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()

            if (s in visited) {
                continue
            }
            visited.add(s)

            if (s.minute > 27 && s.geoRobots == 0) {
                // got this far with no geo robots, so go no further. 27 is arbitrary!
                continue
            }

            // Add all possibilities
            if (s.minute < maxMinutes) {
                if (s.ore >= b.geoCostOre && s.obs >= b.geoCostObs) {
                    queue.addLast(
                        // Make geo robot
                        s.copy(
                            minute = s.minute + 1,
                            ore = s.ore + s.oreRobots - b.geoCostOre,
                            clay = s.clay + s.clayRobots,
                            obs = s.obs + s.obsRobots - b.geoCostObs,
                            geo = s.geo + s.geoRobots,
                            geoRobots = s.geoRobots + 1
                        )
                    )
                    continue // don't investigate other options
                }

                queue.addLast(
                    // Just accumulate minerals
                    s.copy(
                        minute = s.minute + 1,
                        ore = s.ore + s.oreRobots,
                        clay = s.clay + s.clayRobots,
                        obs = s.obs + s.obsRobots,
                        geo = s.geo + s.geoRobots
                    )
                )

                if (s.ore >= b.oreCostOre && s.oreRobots < maxOreRobotsNeeded) {
                    queue.addLast(
                        // Make ore robot
                        s.copy(
                            minute = s.minute + 1,
                            ore = s.ore + s.oreRobots - b.oreCostOre,
                            clay = s.clay + s.clayRobots,
                            obs = s.obs + s.obsRobots,
                            geo = s.geo + s.geoRobots,
                            oreRobots = s.oreRobots + 1
                        )
                    )
                }

                if (s.ore >= b.clayCostOre && s.clayRobots < maxClayRobotsNeeded) {
                    queue.addLast(
                        // Make clay robot
                        s.copy(
                            minute = s.minute + 1,
                            ore = s.ore + s.oreRobots - b.clayCostOre,
                            clay = s.clay + s.clayRobots,
                            obs = s.obs + s.obsRobots,
                            geo = s.geo + s.geoRobots,
                            clayRobots = s.clayRobots + 1
                        )
                    )
                }

                if (s.ore >= b.obsCostOre && s.clay >= b.obsCostClay && s.obsRobots < maxObsRobotsNeeded) {
                    queue.addLast(
                        // Make obs robot
                        s.copy(
                            minute = s.minute + 1,
                            ore = s.ore + s.oreRobots - b.obsCostOre,
                            clay = s.clay + s.clayRobots - b.obsCostClay,
                            obs = s.obs + s.obsRobots,
                            geo = s.geo + s.geoRobots,
                            obsRobots = s.obsRobots + 1
                        )
                    )
                }


            } else {
                // Out of time
                if (s.geo > maxGeo) {
                    maxGeo = s.geo
                }
            }
        }

        return maxGeo
    }

    fun part1(): Int {
        val state = State(0, 1, 0, 0, 0, 0, 0, 0, 0)

        var quality = 0

        blueprints.forEachIndexed { index, blueprint -> quality += (index + 1) * bfs(state, blueprint, 24) }

        return quality
    }

    fun part2(): Int {
        val state = State(0, 1, 0, 0, 0, 0, 0, 0, 0)

        var result = 1
        for (i in 0 until 3) {
            result *= bfs(state, blueprints[i], 32)
        }

        return result
    }
}

fun main() {
    val aoc = Day19("day19/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}