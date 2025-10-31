package pt.isel.cher

import org.junit.Assert.assertEquals
import org.junit.Test
import pt.isel.cher.domain.Player

class PlayerTest {
    @Test
    fun `opponent property returns correct player`() {
        assertEquals(Player.WHITE, Player.BLACK.opponent)
        assertEquals(Player.BLACK, Player.WHITE.opponent)
    }

    @Test
    fun `toString returns display name`() {
        assertEquals("BLACK", Player.BLACK.toString())
        assertEquals("WHITE", Player.WHITE.toString())
    }
}
