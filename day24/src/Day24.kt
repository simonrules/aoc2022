import java.io.File

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
enum class Direction { UP, DOWN, LEFT, RIGHT }
data class Snowflake(var location: Point, val dir: Direction)

class Day24(path: String) {
    private val minX: Int
    private val maxX: Int
    private val minY: Int
    private val maxY: Int
    private val numStates: Int
    private val blizzards = mutableListOf<Set<Point>>()
    private val direction = mapOf(
        '^' to Direction.UP,
        'v' to Direction.DOWN,
        '<' to Direction.LEFT,
        '>' to Direction.RIGHT
    )

    init {
        val lines = File(path).readLines()
        val width = lines[0].length
        val height = lines.size
        minX = 1
        maxX = width - 2
        minY = 1
        maxY = height - 2
        numStates = (width - 2) * (height - 2)

        // Load first blizzard state
        val firstBlizzard = mutableSetOf<Snowflake>()
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val p = Point(x, y)
                val dir = direction[lines[y][x]]
                if (dir != null) {
                    firstBlizzard.add(Snowflake(p, dir))
                }
            }
        }

        // Compute all possible blizzard states
        var blizzard = firstBlizzard.toSet()
        for (i in 0 until numStates) {
            blizzards.add(blizzard.map { it.location }.toSet())
            blizzard = iterate(blizzard)
        }
    }

    private fun iterate(snowflakes: Set<Snowflake>): Set<Snowflake> {
        val newSnowflakes = mutableSetOf<Snowflake>()

        snowflakes.forEach { s ->
            val p = s.location
            val newP = when (s.dir) {
                Direction.UP -> {
                    if (p.y > minY) {
                        Point(p.x, p.y - 1)
                    } else {
                        Point(p.x, maxY)
                    }
                }

                Direction.DOWN -> {
                    if (p.y < maxY) {
                        Point(p.x, p.y + 1)
                    } else {
                        Point(p.x, minY)
                    }
                }

                Direction.LEFT -> {
                    if (p.x > minX) {
                        Point(p.x - 1, p.y)
                    } else {
                        Point(maxX, p.y)
                    }
                }

                Direction.RIGHT -> {
                    if (p.x < maxX) {
                        Point(p.x + 1, p.y)
                    } else {
                        Point(minX, p.y)
                    }
                }
            }
            newSnowflakes.add(Snowflake(newP, s.dir))
        }

        return newSnowflakes.toSet()
    }

    data class State(val point: Point, val minute: Int)

    private fun bfs(start: Point, destination: Point, startMinute: Int): Int {
        val visited = HashSet<State>()
        val queue = ArrayDeque<State>()

        queue.add(State(start, startMinute))

        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()

            if (s in visited) {
                continue
            }
            visited.add(s)

            // Check to see if dest was reached
            if (s.point == destination) {
                // found
                return s.minute + 1
            }

            // Explore other options in time + 1
            val nextBlizzard = blizzards[(s.minute + 1) % numStates]

            // Wait in place
            // Could optimise here: this allows indefinite waiting at the start
            if (s.point !in nextBlizzard) {
                queue.addLast(State(s.point, s.minute + 1))
            }

            // Up
            if (s.point.y > minY) {
                val nextPoint = Point(s.point.x, s.point.y - 1)
                if (nextPoint !in nextBlizzard) {
                    queue.addLast(State(nextPoint, s.minute + 1))
                }
            }

            // Down
            if (s.point.y < maxY) {
                val nextPoint = Point(s.point.x, s.point.y + 1)
                if (nextPoint !in nextBlizzard) {
                    queue.addLast(State(nextPoint, s.minute + 1))
                }
            }

            // Left
            if (s.point.y in minY..maxY && s.point.x > minX) {
                val nextPoint = Point(s.point.x - 1, s.point.y)
                if (nextPoint !in nextBlizzard) {
                    queue.addLast(State(nextPoint, s.minute + 1))
                }
            }

            // Right
            if (s.point.y in minY..maxY && s.point.x < maxX) {
                val nextPoint = Point(s.point.x + 1, s.point.y)
                if (nextPoint !in nextBlizzard) {
                    queue.addLast(State(nextPoint, s.minute + 1))
                }
            }
        }

        // oops
        return 0
    }

    fun part1(): Int {
        return bfs(Point(minX, 0), Point(maxX, maxY), 0)
    }

    fun part2(): Int {
        val leg1 = bfs(Point(minX, 0), Point(maxX, maxY), 0)
        val leg2 = bfs(Point(maxX, maxY + 1), Point(minX, minY), leg1)
        val leg3 = bfs(Point(minX, 0), Point(maxX, maxY), leg2)

        return leg3
    }
}

fun main() {
    val aoc = Day24("day24/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}
