package pt.isel.cher.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
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
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.MoveEntity
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteGameDaoTest {
    private lateinit var database: CheRDatabase
    private lateinit var dao: FavoriteGameDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room
                .inMemoryDatabaseBuilder(context, CheRDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.favoriteGameDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetFavoriteGameWithMoves() =
        runBlocking {
            val favoriteGame =
                FavoriteGame(
                    title = "Test Game",
                    opponentName = "Opponent",
                    dateTime = System.currentTimeMillis(),
                )

            dao.insertFavoriteGame(favoriteGame)

            val retrievedGameFlow = dao.getFavoriteGameWithMovesById(favoriteGame.id)
            val retrievedGame = retrievedGameFlow.first()

            assertNotNull(retrievedGame)
            assertEquals(favoriteGame.id, retrievedGame.favoriteGame.id)
            assertEquals(favoriteGame.title, retrievedGame.favoriteGame.title)
            assertEquals(favoriteGame.opponentName, retrievedGame.favoriteGame.opponentName)

            assertTrue(retrievedGame.moves.isEmpty())
        }

    @Test
    fun insertMovesAndRetrieveFavoriteGameWithMoves() =
        runBlocking {
            val favoriteGame =
                FavoriteGame(
                    title = "Test Game with Moves",
                    opponentName = "Player",
                    dateTime = System.currentTimeMillis(),
                )

            dao.insertFavoriteGame(favoriteGame)

            val moves =
                listOf(
                    MoveEntity(
                        favoriteGameId = favoriteGame.id,
                        moveNumber = 1,
                        row = 2,
                        col = 3,
                        player = "BLACK",
                    ),
                    MoveEntity(
                        favoriteGameId = favoriteGame.id,
                        moveNumber = 2,
                        row = 2,
                        col = 2,
                        player = "WHITE",
                    ),
                )

            dao.insertMoves(moves)

            val retrievedGameFlow = dao.getFavoriteGameWithMovesById(favoriteGame.id)
            val retrievedGame = retrievedGameFlow.first()

            assertNotNull(retrievedGame)
            assertEquals(favoriteGame.id, retrievedGame.favoriteGame.id)
            assertEquals(2, retrievedGame.moves.size)
            assertEquals(moves[0].row, retrievedGame.moves[0].row)
            assertEquals(moves[0].col, retrievedGame.moves[0].col)
            assertEquals(moves[0].player, retrievedGame.moves[0].player)
            assertEquals(moves[1].row, retrievedGame.moves[1].row)
            assertEquals(moves[1].col, retrievedGame.moves[1].col)
            assertEquals(moves[1].player, retrievedGame.moves[1].player)
        }

    @Test
    fun getAllFavoriteGamesWithMoves() =
        runBlocking {
            val game1 =
                FavoriteGame(
                    title = "Game 1",
                    opponentName = "Player A",
                    dateTime = System.currentTimeMillis(),
                )

            val game2 =
                FavoriteGame(
                    title = "Game 2",
                    opponentName = "Player B",
                    dateTime = System.currentTimeMillis() + 1000,
                )

            dao.insertFavoriteGame(game1)
            dao.insertFavoriteGame(game2)

            val movesGame1 =
                listOf(
                    MoveEntity(
                        favoriteGameId = game1.id,
                        moveNumber = 1,
                        row = 2,
                        col = 3,
                        player = "BLACK",
                    ),
                )
            dao.insertMoves(movesGame1)

            val movesGame2 =
                listOf(
                    MoveEntity(
                        favoriteGameId = game2.id,
                        moveNumber = 1,
                        row = 3,
                        col = 4,
                        player = "WHITE",
                    ),
                    MoveEntity(
                        favoriteGameId = game2.id,
                        moveNumber = 2,
                        row = 4,
                        col = 5,
                        player = "BLACK",
                    ),
                )
            dao.insertMoves(movesGame2)

            val allGamesFlow = dao.getAllFavoriteGamesWithMoves()
            val allGames = allGamesFlow.first()

            assertEquals(2, allGames.size)
            assertEquals(game2.id, allGames[0].favoriteGame.id)
            assertEquals(game1.id, allGames[1].favoriteGame.id)

            assertEquals(2, allGames[0].moves.size)
            assertEquals(movesGame2[0].row, allGames[0].moves[0].row)
            assertEquals(1, allGames[1].moves.size)
            assertEquals(movesGame1[0].row, allGames[1].moves[0].row)
        }

    @Test
    fun deleteFavoriteGame() =
        runBlocking {
            val favoriteGame =
                FavoriteGame(
                    title = "Game to Delete",
                    opponentName = "Player Delete",
                    dateTime = System.currentTimeMillis(),
                )

            dao.insertFavoriteGame(favoriteGame)

            val move =
                MoveEntity(
                    favoriteGameId = favoriteGame.id,
                    moveNumber = 1,
                    row = 2,
                    col = 3,
                    player = "BLACK",
                )
            dao.insertMoves(listOf(move))

            var retrievedGame = dao.getFavoriteGameWithMovesById(favoriteGame.id).first()
            assertNotNull(retrievedGame)
            assertEquals(1, retrievedGame.moves.size)

            dao.deleteFavoriteGame(favoriteGame)

            retrievedGame = dao.getFavoriteGameWithMovesById(favoriteGame.id).first()
            assertNull(retrievedGame)
        }

    @Test
    fun insertFavoriteGameWithMoves() =
        runBlocking {
            val favoriteGame =
                FavoriteGame(
                    title = "Game with Repository Insert",
                    opponentName = "Player Repo",
                    dateTime = System.currentTimeMillis(),
                )

            val moves =
                listOf(
                    MoveEntity(
                        favoriteGameId = favoriteGame.id,
                        moveNumber = 1,
                        row = 3,
                        col = 3,
                        player = "WHITE",
                    ),
                    MoveEntity(
                        favoriteGameId = favoriteGame.id,
                        moveNumber = 2,
                        row = 4,
                        col = 4,
                        player = "BLACK",
                    ),
                )

            dao.insertFavoriteGameWithMoves(favoriteGame, moves)

            val retrievedGame = dao.getFavoriteGameWithMovesById(favoriteGame.id).first()
            assertNotNull(retrievedGame)
            assertEquals(2, retrievedGame.moves.size)
            assertEquals(moves[0].row, retrievedGame.moves[0].row)
            assertEquals(moves[1].row, retrievedGame.moves[1].row)
        }
}
