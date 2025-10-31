package pt.isel.cher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Board.Companion.BOARD_SIZE
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position

class GameTest {
    private lateinit var game: Game

    @Before
    fun setUp() {
        game = Game()
    }

    @Test
    fun `initial game state is correct`() {
        assertNotNull(game.id)
        assertEquals(Player.BLACK, game.currentPlayer)
        assertFalse(game.isOver)
        assertNull(game.winner)
        assertEquals(0, game.board.moves.size)
        assertEquals(2, game.board.score(Player.BLACK))
        assertEquals(2, game.board.score(Player.WHITE))
    }

    @Test
    fun `playing a valid move updates the game state`() {
        val position = Position(2, 3)
        val newGame = game.playMove(position)

        assertNotEquals(game, newGame)
        assertEquals(1, newGame.board.moves.size)
        assertEquals(Player.WHITE, newGame.currentPlayer)
        assertFalse(newGame.isOver)
        assertNull(newGame.winner)

        assertEquals(Player.BLACK, newGame.board.grid[2][3])
        assertEquals(Player.BLACK, newGame.board.grid[3][3])
    }

    @Test
    fun `playing an invalid move does not change the game state`() {
        val invalidPosition = Position(0, 0)
        val newGame = game.playMove(invalidPosition)

        assertEquals(game, newGame)
    }

    @Test
    fun `game switches players correctly`() {
        var currentGame = game.playMove(Position(2, 3))
        assertEquals(Player.WHITE, currentGame.currentPlayer)

        currentGame = currentGame.playMove(Position(2, 2))
        assertEquals(Player.BLACK, currentGame.currentPlayer)
    }

    @Test
    fun `game does not switch to opponent if opponent has no valid moves`() {
        val customGrid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { Player.BLACK } }
        val customBoard = Board(grid = customGrid.map { it.toList() }, moves = listOf())
        val customGame = Game(board = customBoard, currentPlayer = Player.BLACK)

        val newGame = customGame.playMove(Position(0, 1))
        assertEquals(Player.BLACK, newGame.currentPlayer)
    }

    @Test
    fun `game is over when no players have valid moves`() {
        val fullGrid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { Player.BLACK } }
        val fullBoard = Board(grid = fullGrid.map { it.toList() }, moves = listOf())
        val fullGame = Game(board = fullBoard, isOver = true, winner = Player.BLACK)

        assertTrue(fullGame.isOver)
        assertEquals(Player.BLACK, fullGame.winner)
    }

    @Test
    fun `determineWinner correctly identifies the winner`() {
        val grid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { Player.BLACK } }
        val board = Board(grid = grid.map { it.toList() }, moves = listOf())
        val gameOver = Game(board = board, isOver = true, winner = Player.BLACK)

        assertTrue(gameOver.isOver)
        assertEquals(Player.BLACK, gameOver.winner)
    }

    @Test
    fun `determineWinner returns null for a tie`() {
        val grid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { Player.BLACK } }
        for (row in 0 until BOARD_SIZE) {
            for (col in 0 until BOARD_SIZE) {
                grid[row][col] = if ((row + col) % 2 == 0) Player.BLACK else Player.WHITE
            }
        }
        val board = Board(grid = grid.map { it.toList() }, moves = listOf())
        val gameOver = Game(board = board, isOver = true, winner = null)

        assertTrue(gameOver.isOver)
        assertNull(gameOver.winner)
    }
}
