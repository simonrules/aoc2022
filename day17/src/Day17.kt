import java.io.File

class Day17(path: String) {
    private val chamberW = 7
    private val fromLeft = 2
    private val fromFloor = 3
    private val jet: String

    private val pixels = arrayOf(
        "####",
        ".#.\n" +
        "###\n" +
        ".#.",
        "###\n" +
        "..#\n" +
        "..#",
        "#\n" +
        "#\n" +
        "#\n" +
        "#",
        "##\n" +
        "##"
    )
    private val widths = arrayOf(4, 3, 3, 1, 2)
    private val heights = arrayOf(1, 3, 3, 4, 2)

    init {
        jet = File(path).readLines()[0]
    }

    data class Rock(var x: Int, val y: Int, val w: Int, val h: Int, val data: String) {
        fun getAt(x: Int, y: Int): Boolean {
            return data[y * (w + 1) + x] == '#'
        }

        fun overlap(other: Rock): Boolean {
            return x.coerceAtLeast(other.x) <= (x + w).coerceAtMost(other.x + other.w) &&
                    y.coerceAtLeast(other.y) <= (y + w).coerceAtMost(other.y + other.w)
        }

        fun collision(other: Rock): Boolean {
            // check for collision: bottom row of a against top row of b

            return false
        }
    }

    fun part1(): Int {

        // Starting state
        var rock0 = Rock(0, 0, widths[0], heights[0], pixels[0])
        var rock1 = Rock(0, 0, widths[1], heights[1], pixels[1])

        println(rock0.overlap(rock1))

        return 0
    }
}

fun main() {
    val aoc = Day17("day17/test1.txt")
    println(aoc.part1())
}