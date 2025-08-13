package org.cluelesshog.game.logic

import org.cluelesshog.game.logic.JewelType.DIAMOND
import org.cluelesshog.game.logic.JewelType.EMERALD
import org.cluelesshog.game.logic.JewelType.RUBY
import org.cluelesshog.game.logic.JewelType.AMETHYST
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardTest {
    private lateinit var board: Board

    @BeforeEach
    fun setup() {
        RNG.setSeed(123)

        board = squareBoardOf(
            "ERD",
            "RDD",
            "DDR"
        )
    }

    @Test
    fun testSwapHappens() {
        assertTrue(board.swap(JewelPos(2, 1), JewelPos(2, 0)).isNotEmpty())
        assertEqualsBoardOf(
            arrayOf(
                "DEA",
                "ERD",
                "RDR"
            ), board
        )
        assertEquals(board.getScore(), 30)

        assertTrue(board.swap(JewelPos(1, 1), JewelPos(1, 0)).isNotEmpty())
        assertEqualsBoardOf(
            arrayOf(
                "EDE",
                "DEA",
                "EDD"
            ), board
        )
        assertEquals(board.getScore(), 90)
    }

    @Test
    fun testSwapDoNotHappens() {
        checkInvalidSwap(JewelPos(0, 0), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(0, 2), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(1, 1), JewelPos(1, 1))
    }

    private fun checkInvalidSwap(first: JewelPos, second: JewelPos) {
        val previousBoard = board.map { it.copy() }

        assertFalse(board.swap(first, second).isNotEmpty())

        val currentBoard = board.map { it.copy() }
        assertEquals(previousBoard, currentBoard)
    }

    private fun gridOf(vararg rows: Map<JewelPos, Jewel>): MutableMap<JewelPos, Jewel> {
        return rows
            .flatMap { it.entries }
            .associate { it.toPair() }
            .toMutableMap()
    }

    private fun rowOf(vararg types: JewelType, row: Int): Map<JewelPos, Jewel> {
        return types
            .mapIndexed { col, type ->
                val pos = JewelPos(col, row)
                Pair(pos, Jewel(pos, type))
            }
            .toMap()
    }

    private fun assertEqualsBoardOf(expected: Array<String>, actual: Board) {
        val expectedBoard = squareBoardOf(*expected)

        assertEquals(expectedBoard.count(), actual.count())

        for (expectedJewel in expectedBoard) {
            val actualJewel = actual.getJewel(expectedJewel.pos)
            assertNotNull(actualJewel)
            assertEquals(
                expectedJewel.type,
                actualJewel.type
            )
        }
    }

    private fun fromCharOrThrow(c: String): JewelType {
        return when (c) {
            "D" -> DIAMOND
            "R" -> RUBY
            "E" -> EMERALD
            "A" -> AMETHYST
            else -> error("Unknown JewelType: $c")
        }
    }

    fun squareBoardOf(vararg lines: String): Board {
        require(lines.all { it.length == lines.size })

        val rows = lines.mapIndexed { arrayIndex, line ->
            val jewelTypes = line.map { char -> fromCharOrThrow(char.toString()) }.toTypedArray<JewelType>()
            val rowIndex = lines.size - 1 - arrayIndex
            rowOf(*jewelTypes, row = rowIndex)
        }.toTypedArray()

        return Board(gridOf(*rows))
    }

}
