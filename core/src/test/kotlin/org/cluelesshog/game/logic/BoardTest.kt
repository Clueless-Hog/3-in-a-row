package org.cluelesshog.game.logic

import org.cluelesshog.game.logic.JewelType.DIAMOND
import org.cluelesshog.game.logic.JewelType.EMERALD
import org.cluelesshog.game.logic.JewelType.RUBY
import org.junit.jupiter.api.Assertions.assertFalse
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
        board = Board(gridOf(
            rowOf(EMERALD, RUBY, DIAMOND, row = 2),
            rowOf(RUBY, DIAMOND, RUBY, row = 1),
            rowOf(DIAMOND, DIAMOND, EMERALD, row = 0),
        ))
    }

    @Test
    fun testSwapHappens() {
        assertTrue(board.swap(JewelPos(1, 2), JewelPos(2, 2)))
        assertEquals(DIAMOND, board.getJewel(JewelPos(1, 2)).type)
        assertEquals(RUBY, board.getJewel(JewelPos(2, 2)).type)
    }

    @Test
    fun testSwapDoNotHappens() {
        assertFalse(board.swap(JewelPos(2, 1), JewelPos(2, 2)))
        assertEquals(DIAMOND, board.getJewel(JewelPos(2, 2)).type)
        assertEquals(RUBY, board.getJewel(JewelPos(2, 1)).type)
    }

}

fun gridOf(vararg rows: Map<JewelPos, Jewel>): MutableMap<JewelPos, Jewel> {
    return rows
        .flatMap { it.entries }
        .associate { it.toPair() }
        .toMutableMap()
}

fun rowOf(vararg types: JewelType, row: Int): Map<JewelPos, Jewel> {
    return types
        .mapIndexed { col, type ->
            val pos = JewelPos(col, row)
            Pair(pos, Jewel(pos, type))
        }
        .toMap()
}
