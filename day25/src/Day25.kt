import java.io.File

class Day25(path: String) {
    private val digits = mapOf('2' to 2, '1' to 1, '0' to 0, '-' to -1, '=' to -2)
    private val sum: Long

    init {
        var total = 0L
        File(path).forEachLine {
            total += parse(it)
        }
        sum = total
    }

    private fun parse(snafu: String): Long {
        var value = 0L
        snafu.forEach {
            value *= 5L
            value += digits[it]!!
        }
        return value
    }

    private fun decimalToSnafu(decimal: Long): String {
        var d = decimal

        // Convert to base 5
        var result = StringBuilder()
        var carry = 0L
        while (d + carry > 0L) {
            when (d % 5 + carry) {
                0L -> {
                    result = StringBuilder("0").append(result)
                    carry = 0L
                }
                1L -> {
                    result = StringBuilder("1").append(result)
                    carry = 0L
                }
                2L -> {
                    result = StringBuilder("2").append(result)
                    carry = 0L
                }
                3L -> {
                    result = StringBuilder("=").append(result)
                    carry = 1L
                }
                4L -> {
                    result = StringBuilder("-").append(result)
                    carry = 1L
                }
                5L -> {
                    result = StringBuilder("0").append(result)
                    carry = 1L
                }
            }
            d /= 5L
        }
        return result.toString()
    }

    fun part1(): String {
        return decimalToSnafu(sum)
    }
}

fun main() {
    val aoc = Day25("day25/input.txt")

    println(aoc.part1())
}
