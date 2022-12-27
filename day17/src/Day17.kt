import java.io.File
import kotlin.math.min

class Day17(path: String) {
    private val chamberW = 7
    private val chamberH = 200
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
    private val rocks = mutableListOf<Rock>()
    private val num = 5

    private val chamber = Array(chamberW * chamberH) { '.' }

    init {
        jet = File(path).readLines()[0]
    }

    data class Rock(val x: Int, val y: Int, val w: Int, val h: Int, val data: String) {
        fun getAt(x: Int, y: Int): Boolean {
            return data[y * (w + 1) + x] == '#'
        }

        fun overlap(other: Rock): Boolean {
            return x.coerceAtLeast(other.x) <= (x + w).coerceAtMost(other.x + other.w) &&
                    y.coerceAtLeast(other.y) <= (y + w).coerceAtMost(other.y + other.w)
        }

        fun collision(other: Rock): Boolean {
            // are they close enough?
            // this == rock on top
            if (y >= other.y + other.h) {
                return false
            }

            val topRow = other.h - 1
            val offset = x - other.x

            // check for collision: bottom row of this against top row of other
            if (other.x >= x && other.x < (x + w)) {
                // this is left of other
                val overlap = (x + w) - other.x
                for (j in 0 until overlap) {
                    if (getAt(j - offset, 0) && other.getAt(j, topRow)) {
                        return true
                    }
                }
            } else if (x >= other.x && x < (other.x + other.w)) {
                // this is right of other
                val overlap = (other.x + other.w) - x
                for (j in 0 until overlap) {
                    if (getAt(j, 0) && other.getAt(j + offset, topRow)) {
                        return true
                    }
                }
            }

            return false
        }
    }

    private fun hasCollided(rock: Rock): Boolean {
        // Floor
        if (rock.y < 0) {
            return true
        }

        // check for collisions with last 7 rocks
        return rocks.takeLast(7).any { rock.collision(it) }
    }

    private fun getTopOfPile(): Int {
        // check the last 7 rocks to see which is the highest
        var highest = 0
        val lastSeven = rocks.takeLast(7)
        lastSeven.forEach { if (it.y + it.h > highest) highest = it.y + it.h }
        return highest
    }

    private fun addToChamber(rock: Rock) {
        for (y in 0 until rock.h) {
            for (x in 0 until rock.w) {
                val cX = x + rock.x
                val cY = y + rock.y
                if (rock.getAt(x, y)) {
                    chamber[cY * chamberW + cX] = '#'
                }
            }
        }
    }

    private fun printChamber(startY: Int) {
        for (y in startY downTo 0) {
            for (x in 0 until chamberW) {
                print(chamber[y * chamberW + x])
            }
            println()
        }
        println()
    }

    fun part1(): Int {
        val fromLeft = 2
        var startY = 3 // start 3 pixels from the floor

        var j = 0
        for (r in 0 until 10) { // 2022
            var rock = Rock(fromLeft, startY, widths[r % num], heights[r % num], pixels[r % num])

            while (true) {
                // move rock l/r
                var newX = if (jet[j++ % jet.length] == '>') rock.x + 1 else rock.x - 1

                // clamp to left and right edges of chamber
                if (newX < 0) {
                    newX = 0
                } else if (newX + rock.w > chamberW) {
                    newX = chamberW - rock.w
                }

                var newRock = Rock(newX, rock.y, rock.w, rock.h, rock.data)
                if (hasCollided(newRock)) break
                rock = newRock

                // move rock down
                val newY = newRock.y - 1
                newRock = Rock(newRock.x, newY, newRock.w, newRock.h, newRock.data)
                if (hasCollided(newRock)) break
                rock = newRock
            }

            // add rock's final position
            rocks.add(rock)
            addToChamber(rock)
            startY = getTopOfPile() + 3
            printChamber(startY)
        }

        return 0
    }
}

fun main() {
    val aoc = Day17("day17/test1.txt")
    println(aoc.part1())
}