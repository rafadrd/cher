package pt.isel.cher.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.cher.data.database.CheRDatabase
import pt.isel.cher.data.database.dao.FavoriteGameDao
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteGameRepositoryTest {
    private lateinit var database: CheRDatabase
    private lateinit var dao: FavoriteGameDao
    private lateinit var repository: FavoriteGameRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room
                .inMemoryDatabaseBuilder(context, CheRDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.favoriteGameDao()
        repository = FavoriteGameRepository(dao)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun addFavoriteGameAndRetrieve() =
        runBlocking {
            val gameId = "test-game-id"
            var gameBoard =
                Board(
                    moves =
                        listOf(
                            Move(Position(2, 3), Player.BLACK),
                            Move(Position(2, 2), Player.WHITE),
                        ),
                )
            val gameInstance =
                Game(
                    id = gameId,
                    board = gameBoard,
                    currentPlayer = Player.WHITE,
                    isOver = true,
                    winner = Player.BLACK,
                )

            repository.addFavoriteGame(
                game = gameInstance,
                title = "Champion Match",
                opponentName = "Opponent",
            )

            val allGames = repository.getAllFavoriteGames().first()
            assertEquals(1, allGames.size)
            val retrievedGameWithMoves = allGames[0]
            assertEquals("Champion Match", retrievedGameWithMoves.favoriteGame.title)
            assertEquals("Opponent", retrievedGameWithMoves.favoriteGame.opponentName)

            assertEquals(2, retrievedGameWithMoves.moves.size)
            assertEquals(2, retrievedGameWithMoves.moves[0].row)
            assertEquals(3, retrievedGameWithMoves.moves[0].col)
            assertEquals("BLACK", retrievedGameWithMoves.moves[0].player)
            assertEquals(2, retrievedGameWithMoves.moves[1].row)
            assertEquals(2, retrievedGameWithMoves.moves[1].col)
            assertEquals("WHITE", retrievedGameWithMoves.moves[1].player)
        }

    @Test
    fun deleteFavoriteGame() =
        runBlocking {
            val gameId = "delete-game-id"
            val gameBoard =
                Board(
                    moves =
                        listOf(
                            Move(Position(3, 3), Player.WHITE),
                            Move(Position(4, 4), Player.BLACK),
                        ),
                )
            val gameInstance =
                Game(
                    id = gameId,
                    board = gameBoard,
                    currentPlayer = Player.BLACK,
                    isOver = true,
                    winner = Player.WHITE,
                )

            repository.addFavoriteGame(
                game = gameInstance,
                title = "Delete Match",
                opponentName = "Player B",
            )

            var allGames = repository.getAllFavoriteGames().first()
            assertEquals(1, allGames.size)

            val favoriteGame = allGames[0].favoriteGame
            repository.deleteFavoriteGame(favoriteGame)

            allGames = repository.getAllFavoriteGames().first()
            assertTrue(allGames.isEmpty())
        }

    @Test
    fun getFavoriteGameById() =
        runBlocking {
            val gameId1 = "game-id-1"
            val gameBoard1 = Board(moves = listOf(Move(Position(1, 1), Player.BLACK)))
            val gameInstance1 =
                Game(
                    id = gameId1,
                    board = gameBoard1,
                    currentPlayer = Player.WHITE,
                    isOver = true,
                    winner = Player.BLACK,
                )

            repository.addFavoriteGame(
                game = gameInstance1,
                title = "Solo Match",
                opponentName = "Player Solo",
            )

            val gameId2 = "game-id-2"
            val gameBoard2 =
                Board(
                    moves =
                        listOf(
                            Move(Position(2, 2), Player.WHITE),
                            Move(Position(3, 3), Player.BLACK),
                        ),
                )
            val gameInstance2 =
                Game(
                    id = gameId2,
                    board = gameBoard2,
                    currentPlayer = Player.BLACK,
                    isOver = true,
                    winner = Player.WHITE,
                )

            repository.addFavoriteGame(
                game = gameInstance2,
                title = "Duo Match",
                opponentName = "Player Duo",
            )

            val retrievedGame1 = repository.getFavoriteGameById(gameId1).first()
            assertNotNull(retrievedGame1)
            assertEquals("Solo Match", retrievedGame1.favoriteGame.title)
            assertEquals(1, retrievedGame1.moves.size)
            assertEquals(1, retrievedGame1.moves[0].row)
            assertEquals(1, retrievedGame1.moves[0].col)
            assertEquals("BLACK", retrievedGame1.moves[0].player)

            val retrievedGame2 = repository.getFavoriteGameById(gameId2).first()
            assertNotNull(retrievedGame2)
            assertEquals("Duo Match", retrievedGame2.favoriteGame.title)
            assertEquals(2, retrievedGame2.moves.size)
            assertEquals(2, retrievedGame2.moves[0].row)
            assertEquals(2, retrievedGame2.moves[0].col)
            assertEquals("WHITE", retrievedGame2.moves[0].player)
            assertEquals(3, retrievedGame2.moves[1].row)
            assertEquals(3, retrievedGame2.moves[1].col)
            assertEquals("BLACK", retrievedGame2.moves[1].player)

            val nonExistentGame = repository.getFavoriteGameById("non-existent-id").first()
            assertNull(nonExistentGame)
        }
}
