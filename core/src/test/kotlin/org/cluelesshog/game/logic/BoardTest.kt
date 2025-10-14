package org.cluelesshog.game.logic

import engine.event.EventBus
import org.cluelesshog.game.logic.JewelType.DIAMOND
import org.cluelesshog.game.logic.JewelType.EMERALD
import org.cluelesshog.game.logic.JewelType.RUBY
import org.cluelesshog.game.logic.JewelType.AMETHYST
import org.cluelesshog.game.logic.event.JewelSwapped
import org.cluelesshog.game.logic.event.Match
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardTest {
    private lateinit var board: Board
    private var isSwapped = false

    @BeforeEach
    fun setup() {
        RNG.setSeed(123)

        EventBus.subscribe<JewelSwapped> {
            isSwapped = true
        }

        board = squareBoardOf(
            "ERD",
            "RDD",
            "DDR"
        )
    }

    @Test
    fun testSwapHappens() {
        val firstSwap = EventBus.subscribe<Match> {
            assertMatches(
                listOf(
                    JewelPos(0, 0),
                    JewelPos(1, 0),
                    JewelPos(2, 0)
                ), it
            )
            assertJewelsFellDown(it)
            assertNewJewels(
                listOf(
                    Jewel(JewelPos(0, 2), DIAMOND),
                    Jewel(JewelPos(1, 2), EMERALD),
                    Jewel(JewelPos(2, 2), AMETHYST)
                ), it
            )
            assertEquals(30, it.scoreUp)
        }

        board.swap(JewelPos(2, 1), JewelPos(2, 0))
        assertTrue(isSwapped)

        assertEqualsBoardOf(
            arrayOf(
                "DEA",
                "ERD",
                "RDR"
            ), board
        )
        EventBus.unsubscribe(firstSwap)

        isSwapped = false
        val secondSwap = EventBus.subscribe<Match>{
            assertMatches(
                listOf(
                    JewelPos(0, 0),
                    JewelPos(1, 0),
                    JewelPos(2, 0)
                ), it
            )
            assertJewelsFellDown(it)
            assertNewJewels(
                listOf(
                    Jewel(JewelPos(0, 2), EMERALD),
                    Jewel(JewelPos(1, 2), DIAMOND),
                    Jewel(JewelPos(2, 2), EMERALD)
                ), it
            )
            assertEquals(90, it.scoreUp)
        }

        board.swap(JewelPos(1, 1), JewelPos(1, 0))
        assertTrue(isSwapped)

        assertEqualsBoardOf(
            arrayOf(
                "EDE",
                "DEA",
                "EDD"
            ), board
        )
        EventBus.unsubscribe(secondSwap)
    }

    private fun assertNewJewels(
        expected: List<Jewel>,
        actual: Match
    ) {
        assertEquals(expected, actual.newJewels)
    }

    private fun assertMatches(
        expected: List<JewelPos>,
        actual: Match
    ) {
        assertEquals(expected, actual.matches)
    }

    private fun assertEquals(
        expected: Collection<Any>,
        actual: Collection<Any>
    ) {
        fun <T> Collection<T>.frequencyMap(): Map<T, Int> =
            this.groupingBy { it }.eachCount()

        val expectedFreq = expected.frequencyMap()
        val actualFreq = actual.frequencyMap()

        assertEquals(expectedFreq, actualFreq)
    }

    private fun assertJewelsFellDown(result: Match) {
        val matches = result.matches
        val movedJewels = result.movedJewels

        val matchesByCol = matches.groupBy { it.column }.mapValues { entry -> entry.value.map { it.row } }

        for ((pos, steps) in movedJewels) {
            val matchRowsInCol = matchesByCol[pos.column] ?: emptyList()
            val matchesBelow = matchRowsInCol.count { it < pos.row }
            assertEquals(matchesBelow, steps)
        }
    }

    @Test
    fun testSwapDoNotHappens() {
        checkInvalidSwap(JewelPos(0, 0), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(0, 2), JewelPos(2, 0))
        checkInvalidSwap(JewelPos(1, 1), JewelPos(1, 1))
    }

    private fun checkInvalidSwap(first: JewelPos, second: JewelPos) {
        val previousBoard = board.map { it.copy() }

        isSwapped = false

        board.swap(first, second)
        assertFalse(isSwapped)

        val currentBoard = board.map { it.copy() }
        kotlin.test.assertEquals(previousBoard, currentBoard)
    }

    private fun gridOf(vararg rows: Map<JewelPos, Jewel>) = rows
        .flatMap { it.entries }
        .associate { it.toPair() }
        .toMutableMap()

    private fun rowOf(vararg types: JewelType, row: Int) = types
        .mapIndexed { col, type ->
            val pos = JewelPos(col, row)
            Pair(pos, Jewel(pos, type))
        }
        .toMap()

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

    private fun fromCharOrThrow(c: String) = when (c) {
        "D" -> DIAMOND
        "R" -> RUBY
        "E" -> EMERALD
        "A" -> AMETHYST
        else -> error("Unknown JewelType: $c")
    }

}
