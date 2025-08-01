package org.cluelesshog.game.logic

import org.cluelesshog.game.logic.JewelType.DIAMOND
import org.cluelesshog.game.logic.JewelType.EMERALD
import org.cluelesshog.game.logic.JewelType.RUBY
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
        assertTrue(board.swap(JewelPos(1, 2), JewelPos(2, 2)))
        assertEqualsBoardOf(
            arrayOf(
                "EER",
                "RDD",
                "DRR"
            ), board
        )
        assertEquals(board.getScore(), 30)

        assertTrue(board.swap(JewelPos(0, 0), JewelPos(0, 1)))
        assertEqualsBoardOf(
            arrayOf(
                "REE",
                "DRR",
                "EER"
            ), board
        )
        assertEquals(board.getScore(), 180)
    }

    @Test
    fun testSwapDoNotHappens() {
        checkInvalidSwap(JewelPos(0, 0), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(0, 2), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(1, 1), JewelPos(1, 1))
    }

    private fun checkInvalidSwap(first: JewelPos, second: JewelPos) {
        val previousBoard = board.map { it.copy() }

        assertFalse(board.swap(first, second))

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

    private fun squareBoardOf(vararg lines: String): Board {
        require(lines.all { it.length == lines.size })

        val rows = lines.mapIndexed { arrayIndex, line ->
            val jewelTypes = line.map { char -> fromCharOrThrow(char.toString()) }.toTypedArray<JewelType>()
            val rowIndex = lines.size - 1 - arrayIndex
            rowOf(*jewelTypes, row = rowIndex)
        }.toTypedArray()

        return Board(gridOf(*rows))
    }

    private fun fromCharOrThrow(c: String): JewelType {
        return when (c) {
            "D" -> DIAMOND
            "R" -> RUBY
            "E" -> EMERALD
            else -> error("Unknown JewelType: $c")
        }
    }

}
