package pt.isel.cher

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import pt.isel.cher.util.Constants.BOARD_SIZE

class BoardTest {
    private lateinit var board: Board

    @Before
    fun setUp() {
        board = Board()
    }

    @Test
    fun `initial grid is correctly initialized`() {
        val expectedGrid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { null } }
        expectedGrid[3][3] = Player.WHITE
        expectedGrid[4][4] = Player.WHITE
        expectedGrid[3][4] = Player.BLACK
        expectedGrid[4][3] = Player.BLACK

        assertEquals(expectedGrid.map { it.toList() }, board.grid)
    }

    @Test
    fun `playing a move updates the grid and flips pieces correctly`() {
        val position = Position(2, 3)
        val newBoard = board.playMove(position, Player.BLACK)
        assertNotNull(newBoard)

        assertEquals(Player.BLACK, newBoard!!.grid[2][3])

        assertEquals(Player.BLACK, newBoard.grid[3][3])
        assertEquals(Player.BLACK, newBoard.grid[3][4])
        assertEquals(Player.BLACK, newBoard.grid[4][3])
        assertEquals(Player.WHITE, newBoard.grid[4][4])
    }

    @Test
    fun `playing an invalid move does not alter the board`() {
        val invalidPosition = Position(3, 3)
        val newBoard = board.playMove(invalidPosition, Player.BLACK)
        assertNull(newBoard)

        assertEquals(Player.WHITE, board.grid[3][3])
    }

    @Test
    fun `score is correctly calculated`() {
        assertEquals(2, board.score(Player.BLACK))
        assertEquals(2, board.score(Player.WHITE))

        val newBoard = board.playMove(Position(2, 3), Player.BLACK)
        assertNotNull(newBoard)
        assertEquals(4, newBoard!!.score(Player.BLACK))
        assertEquals(1, newBoard.score(Player.WHITE))
    }

    @Test
    fun `hasValidMoves correctly identifies available moves`() {
        assertTrue(board.hasValidMoves(Player.BLACK))
        assertTrue(board.hasValidMoves(Player.WHITE))

        var currentBoard = board.playMove(Position(2, 3), Player.BLACK)
        assertNotNull(currentBoard)

        assertTrue(currentBoard!!.hasValidMoves(Player.WHITE))
    }

    @Test
    fun `isGameOver correctly identifies game end`() {
        assertFalse(board.isGameOver())

        val fullGrid = List(BOARD_SIZE) { List(BOARD_SIZE) { Player.BLACK } }
        val fullBoard = board.copy(grid = fullGrid)
        assertTrue(fullBoard.isGameOver())

        val noMoveBoard = board.copy(grid = List(BOARD_SIZE) { List(BOARD_SIZE) { Player.BLACK } })
        assertTrue(noMoveBoard.isGameOver())
    }

    @Test
    fun `playing a move that results in no flips is invalid`() {
        val invalidPosition = Position(0, 0)
        val newBoard = board.playMove(invalidPosition, Player.BLACK)
        assertNull(newBoard)
    }
}
