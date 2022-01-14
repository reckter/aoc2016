package me.reckter.aoc.days

import me.reckter.aoc.Day
import me.reckter.aoc.allPairings
import me.reckter.aoc.cords.d2.Cord2D
import me.reckter.aoc.cords.d2.getNeighbors
import me.reckter.aoc.dijkstra
import me.reckter.aoc.dijkstraInt
import me.reckter.aoc.parseWithRegex
import me.reckter.aoc.print
import me.reckter.aoc.solution
import me.reckter.aoc.solve

class Day22 : Day {
    override val day = 22

    data class Node(
        val pos: Cord2D<Int>,
        val size: Int,
        val used: Int,
        val avail: Int,
    )

    val nodes by lazy {
        loadInput()
            .drop(2)
            .parseWithRegex("/dev/grid/node-x(\\d+)-y(\\d+)\\W+(\\d+)T\\W+(\\d+)T\\W+(\\d+).*")
            .map { (xStr, yStr, sizeStr, usedStr, availStr) ->
                Node(
                    Cord2D(
                        xStr.toInt(),
                        yStr.toInt()
                    ), sizeStr.toInt(), usedStr.toInt(), availStr.toInt()
                )
            }
    }

    override fun solvePart1() {
        nodes
            .allPairings(false)
            .count { (a, b) -> a.used > 0 && b.avail > a.used }
            .solution(1)
    }

    override fun solvePart2() {
        val map = nodes.associateBy { it.pos }

        val minX = map.keys.minOf { it.x }
        val maxX = map.keys.maxOf { it.x }
        val minY = map.keys.minOf { it.y }
        val maxY = map.keys.maxOf { it.y }

        val empty = nodes.find { it.used == 0 }!!

        val path = dijkstraInt(
            empty.pos,
            Cord2D(maxX - 1, 0),
            { it -> it.getNeighbors(true).filter {
                (map[it]?.size ?: Int.MAX_VALUE) < 100 } },
            { _,_ -> 1 }
        )


        val steps = (maxX - minX - 1) * 5 + path.second + 1

        steps.solution(2)
        path.second.print("length")

//
//        (minY..maxY).forEach { y ->
//            (minX..maxX)
//                .forEach { x ->
//                    val node = map[Cord2D(x, y)]!!
//                    val percentage = node.avail.toDouble() / node.size.toDouble()
//
//                    print(
//                        when {
//                            node.size > 100 -> "#"
//                            percentage > 0.5 -> "_"
//                            else -> "."
//                        }
//                    )
//                }
//            print("\n")
//        }
    }
}

fun main() = solve<Day22>()
