import java.io.File

class Day22(input: String) {
    private val map: List<String>/*mutableListOf<String>()*/
    private val path: List<String>
    private var height = 0
    private val start: Point

    enum class Direction {
        FACING_UP,
        FACING_RIGHT,
        FACING_DOWN,
        FACING_LEFT;

        fun turn(towards: Char): Direction {
            return if (towards == 'L') {
                when (this) {
                    FACING_UP -> FACING_LEFT
                    FACING_RIGHT -> FACING_UP
                    FACING_DOWN -> FACING_RIGHT
                    FACING_LEFT -> FACING_DOWN
                }
            } else {
                when (this) {
                    FACING_UP -> FACING_RIGHT
                    FACING_RIGHT -> FACING_DOWN
                    FACING_DOWN -> FACING_LEFT
                    FACING_LEFT -> FACING_UP
                }
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun move(direction: Direction, distance: Int = 1): Point {
            return when (direction) {
                Direction.FACING_UP -> Point(x, y - distance)
                Direction.FACING_RIGHT -> Point(x + distance, y)
                Direction.FACING_DOWN -> Point(x, y + distance)
                Direction.FACING_LEFT -> Point(x - distance, y)
            }
        }
    }

    data class Position(val point: Point, val direction: Direction) {
        fun move(distance: Int = 1): Position {
            return Position(point.move(direction, distance), direction)
        }

        fun turn(towards: Char): Position {
            return Position(point, direction.turn(towards))
        }
    }

    init {
        val file = File(input).readText().split("\n\n")
        val mapBlock = file[0]
        path = splitPath(file[1])
        map = mapBlock.split("\n")
        //mapStrings.forEach { map.add(StringBuilder(it)) }
        height = map.size
        start = Point(getStartX(0), 0)
    }

    private fun splitPath(s: String): List<String> {
        val result = mutableListOf<String>()
        var pos = 0

        while (true) {
            val nextL = s.indexOf('L', pos)
            val nextR = s.indexOf('R', pos)

            if (nextL == -1 && nextR == -1) {
                result.add(s.substring(pos, s.length - 1))
                break
            }

            if (nextR == -1 || nextL < nextR) {
                result.add(s.substring(pos, nextL))
                result.add(s[nextL].toString())
                pos = nextL + 1
            } else if (nextL == -1 || nextR < nextL) {
                result.add(s.substring(pos, nextR))
                result.add(s[nextR].toString())
                pos = nextR + 1
            }
        }

        return result.toList()
    }

    private fun getStartX(y: Int): Int {
        return map[y].lastIndexOf(' ') + 1
    }

    private fun getEndX(y: Int): Int {
        return map[y].length - 1
    }

    private fun getMapAt(p: Point): Char {
        return if (inBounds(p)) map[p.y][p.x] else ' '
    }

    /*private fun setMapAt(p: Point, c: Char) {
        map[p.y][p.x] = c
    }*/

    private fun inBounds(p: Point): Boolean {
        if (p.y < 0 || p.y >= height || p.x < 0 || p.x >= map[p.y].length) {
            return false
        }

        return p.x >= getStartX(p.y) && p.x <= getEndX(p.y)
    }

    private fun printMap() {
        map.forEachIndexed { y, s ->
            val start = getStartX(y)
            val end = getEndX(y)
            println("$s $start - $end")
        }
    }

    private fun handleWrap(position: Position): Position {
        var nextPoint = when (position.direction) {
            Direction.FACING_UP -> Point(position.point.x, height - 1)
            Direction.FACING_DOWN -> Point(position.point.x, 0)
            Direction.FACING_RIGHT ->
                Point(getStartX(position.point.y), position.point.y)

            Direction.FACING_LEFT ->
                Point(getEndX(position.point.y), position.point.y)
        }

        while (!inBounds(nextPoint)) {
            nextPoint = nextPoint.move(position.direction)
        }

        return Position(nextPoint, position.direction)
    }

    private fun handleWrapCube(curPosition: Position): Position {
        val faceSize = height / 3

        val facePos = Point(curPosition.point.x % faceSize, curPosition.point.y % faceSize)
        val faceXId = curPosition.point.x / faceSize
        val faceYId = curPosition.point.y / faceSize

        if (faceXId == 2 && faceYId == 1) {
            // must have fallen off right

            // flip axes
            var newFacePos = Point(faceSize - facePos.y - 1, 0)
            val newFaceXId = 3
            val newFaceYId = 2
            val newDirection = Direction.FACING_DOWN
            newFacePos = Point(newFacePos.x + newFaceXId * faceSize,
                newFacePos.y + newFaceYId * faceSize)

            return Position(newFacePos, newDirection)
        } else if (faceXId == 2 && faceYId == 2) {
            if (curPosition.direction == Direction.FACING_DOWN) {
                var newFacePos = Point(faceSize - facePos.x - 1, facePos.y)
                val newFaceXId = 0
                val newFaceYId = 1
                val newDirection = Direction.FACING_UP
                newFacePos = Point(newFacePos.x + newFaceXId * faceSize,
                    newFacePos.y + newFaceYId * faceSize)

                return Position(newFacePos, newDirection)
            } else {
                // must be facing left
            }
        } else if (faceXId == 1 && faceYId == 1) {
            if (curPosition.direction == Direction.FACING_UP) {
                // swap axes
                var newFacePos = Point(0, facePos.x)
                val newFaceXId = 2
                val newFaceYId = 0
                val newDirection = Direction.FACING_RIGHT
                newFacePos = Point(newFacePos.x + newFaceXId * faceSize,
                    newFacePos.y + newFaceYId * faceSize)

                return Position(newFacePos, newDirection)
            }
        }

        return curPosition
    }

    private fun move(position: Position, distance: Int, cube: Boolean): Position {
        var distLeft = distance
        var curPosition = position

        while (distLeft > 0) {
            var nextPosition = curPosition.move()
            distLeft--

            if (!inBounds(nextPosition.point)) {
                nextPosition = if (cube) {
                    handleWrapCube(curPosition)
                } else {
                    handleWrap(nextPosition)
                }
            }

            if (getMapAt(nextPosition.point) == '#') {
                // Hit a wall
                break
            }

            /*setMapAt(curPosition.point, when (curPosition.direction) {
                Direction.FACING_UP -> '^'
                Direction.FACING_DOWN -> 'v'
                Direction.FACING_RIGHT -> '>'
                Direction.FACING_LEFT -> '<'
            })*/
            curPosition = nextPosition
        }

        return curPosition
    }

    private fun password(position: Position): Int {
        return 4 * (position.point.x + 1) +
                1000 * (position.point.y + 1) +
                when (position.direction) {
                    Direction.FACING_UP -> 3
                    Direction.FACING_DOWN -> 1
                    Direction.FACING_RIGHT -> 0
                    Direction.FACING_LEFT -> 2
                }
    }

    private fun solve(cube: Boolean): Int {
        var position = Position(start, Direction.FACING_RIGHT)

        path.forEach {
            position = if (it[0] == 'L' || it[0] == 'R') {
                position.turn(it[0])
            } else {
                move(position, it.toInt(), cube)
            }
        }

        return password(position)
    }

    fun part1(): Int {
        return solve(false)
    }

    fun part2(): Int {
        return solve(true)
    }
}

fun main() {
    val aoc = Day22("day22/test1.txt")
    println(aoc.part1())
    println(aoc.part2())
}
