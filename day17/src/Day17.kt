import java.io.File
import kotlin.math.min
import kotlin.math.max

class Day17(path: String) {
    private val chamberW = 7
    private val chamberH = 4000
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
                    y.coerceAtLeast(other.y) <= (y + h).coerceAtMost(other.y + other.h)
        }

        fun collision(other: Rock): Boolean {
            if (!overlap(other)) {
                return false
            }

            val offsetX = x - other.x
            val offsetY = y - other.y
            val overlapX = min(x + w, other.x + other.w) - max(x, other.x)
            val overlapY = min(y + h, other.y + other.h) - max(y, other.y)

            // check for collision: all pixels within overlap area
            if (other.x >= x) {
                if (other.y >= y) {
                    // other is above (or level)
                    for (i in 0 until overlapY) {
                        for (j in 0 until overlapX) {
                            if (getAt(j - offsetX, i - offsetY) && other.getAt(j, i)) {
                                return true
                            }
                        }
                    }
                } else {
                    // other is below
                    for (i in 0 until overlapY) {
                        for (j in 0 until overlapX) {
                            if (getAt(j - offsetX, i) && other.getAt(j, i + offsetY)) {
                                return true
                            }
                        }
                    }
                }
            } else {
                if (other.y >= y) {
                    // other is above (or level)
                    for (i in 0 until overlapY) {
                        for (j in 0 until overlapX) {
                            if (getAt(j, i - offsetY) && other.getAt(j + offsetX, i)) {
                                return true
                            }
                        }
                    }
                } else {
                    // other is below
                    for (i in 0 until overlapY) {
                        for (j in 0 until overlapX) {
                            if (getAt(j, i) && other.getAt(j + offsetX, i + offsetY)) {
                                return true
                            }
                        }
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

        // check for collisions
        return rocks.takeLast(20).reversed().any { rock.collision(it) }
    }

    private fun getTopOfPile(): Int {
        // check the last rocks to see which is the highest
        var highest = 0
        val lastSeven = rocks.takeLast(20)
        lastSeven.forEach { if (it.y + it.h > highest) highest = it.y + it.h }
        return highest
    }

    private fun addToChamber(rock: Rock, c: Char) {
        for (y in 0 until rock.h) {
            for (x in 0 until rock.w) {
                val cX = x + rock.x
                val cY = y + rock.y
                if (rock.getAt(x, y)) {
                    chamber[cY * chamberW + cX] = c
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
        for (r in 0 until 2022) {
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

                if (hasCollided(newRock)) {
                } else {
                    rock = newRock
                }

                // move rock down
                val newY = rock.y - 1
                newRock = Rock(rock.x, newY, rock.w, rock.h, rock.data)
                if (hasCollided(newRock)) {
                    break
                }
                rock = newRock
            }

            // add rock's final position
            rocks.add(rock)
            //addToChamber(rock, '#')
            startY = getTopOfPile() + 3
        }

        //printChamber(startY)
        return getTopOfPile()
    }
}

fun main() {
    val aoc = Day17("day17/input.txt")
    println(aoc.part1())
}