package pt.isel.cher

import org.junit.Assert.assertNotNull
import org.junit.Test
import pt.isel.cher.domain.Board.Companion.BOARD_SIZE
import pt.isel.cher.domain.Position

class PositionTest {
    @Test
    fun `valid positions are accepted`() {
        val pos1 = Position(0, 0)
        val pos2 = Position(7, 7)
        val pos3 = Position(3, 4)

        assertNotNull(pos1)
        assertNotNull(pos2)
        assertNotNull(pos3)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `position with negative row throws exception`() {
        Position(-1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `position with negative column throws exception`() {
        Position(0, -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `position with row out of bounds throws exception`() {
        Position(BOARD_SIZE, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `position with column out of bounds throws exception`() {
        Position(0, BOARD_SIZE)
    }
}
