package me.reckter.aoc.days

import me.reckter.aoc.Day
import me.reckter.aoc.matchAndParse
import me.reckter.aoc.permutations
import me.reckter.aoc.print
import me.reckter.aoc.solution
import me.reckter.aoc.solve
import java.lang.Integer.max
import kotlin.math.min

class Day21 : Day {
    override val day = 21

    data class Instruction(
        val x: Int? = null,
        val y: Int? = null,
        val a: String? = null,
        val b: String? = null,
        val type: Type
    ) {
        enum class Type {
            swapPosition,
            swapLetters,
            rotateRight,
            rotateLeft,
            rotateLetter,
            reverseSubString,
            move
        }

        fun apply(str: String): String {
            return when (this.type) {
                Type.swapPosition -> {
                    val min = min(this.x!!, this.y!!)
                    val max = max(this.x!!, this.y!!)
                    str.take(min) + str[max] + str.substring(
                        min + 1,
                        max
                    ) + str[min] + str.drop(max + 1)
                }
                Type.swapLetters -> {
                    str.replace(this.a!!, "_")
                        .replace(this.b!!, this.a)
                        .replace("_", this.b)
                }
                Type.rotateRight ->
                    str.takeLast(this.x!!) + str.dropLast(this.x)
                Type.rotateLeft ->
                    str.drop(this.x!!) + str.take(this.x)
                Type.rotateLetter -> {
                    var index = str.indexOf(this.a!!.first())
                    if(index >= 4) index += 1
                     index++

                    index %= str.length

                    str.takeLast(index) + str.dropLast(index)
                }
                Type.reverseSubString -> {
                    str.replaceRange(this.x!!, this.y!! + 1, str.substring(this.x, this.y + 1).reversed())
                }
                Type.move -> {
                    val tmp = (str.take(this.x!!) + str.drop(this.x + 1))

                    tmp.take(this.y!!) + str[this.x] + tmp.drop(y)
                }
            }
        }
    }

    val instructions by lazy { loadInput()
        .matchAndParse(
            "swap position (\\d+) with position (\\d+)" to { (xStr, yStr) ->
                Instruction(
                    x = xStr.toInt(), y = yStr.toInt(),
                    type = Instruction.Type.swapPosition
                )
            },
            "swap letter (.) with letter (.)" to { (a, b) ->
                Instruction(
                    a = a, b = b,
                    type = Instruction.Type.swapLetters
                )
            },
            "rotate right (\\d+) steps?" to { (xStr) ->
                Instruction(
                    x = xStr.toInt(),
                    type = Instruction.Type.rotateRight
                )
            },
            "rotate left (\\d+) steps?" to { (xStr) ->
                Instruction(
                    x = xStr.toInt(),
                    type = Instruction.Type.rotateLeft
                )
            },
            "rotate based on position of letter (.)" to { (a) ->
                Instruction(
                    a = a,
                    type = Instruction.Type.rotateLetter
                )
            },
            "reverse positions (\\d+) through (\\d+)" to { (xStr, yStr) ->
                Instruction(
                    x = xStr.toInt(), y = yStr.toInt(),
                    type = Instruction.Type.reverseSubString
                )
            },
            "move position (\\d+) to position (\\d+)" to { (xStr, yStr) ->
                Instruction(
                    x = xStr.toInt(), y = yStr.toInt(),
                    type = Instruction.Type.move
                )
            }

        )
    }

    fun scramble(raw: String): String {
        return instructions.fold(raw) { str, cur ->
            cur.apply(str)
        }
    }

    override fun solvePart1() {
        scramble("abcdefgh")
            .solution(1)
    }

    override fun solvePart2() {
        "fbgdceah".toList().permutations()
            .find { scramble(it.joinToString("")) == "fbgdceah" }
            ?.joinToString("")
            .solution(2)
    }
}

fun main() = solve<Day21>()
