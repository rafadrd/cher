package pt.isel.cher.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pt.isel.cher.R
import pt.isel.cher.data.repository.ActiveGameRepository
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position

sealed class GameUiState {
    data class ActiveGame(val game: Game, val validMoves: Set<Position>) : GameUiState()

    data class GameOver(
        val game: Game,
        val winner: Player?,
        val isFavoriteMarked: Boolean = false,
    ) : GameUiState()

    data class Error(val message: String) : GameUiState()
}

@HiltViewModel
class GameViewModel
@Inject
constructor(
    private val favoriteGameRepository: FavoriteGameRepository,
    private val activeGameRepository: ActiveGameRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<GameUiState>(
            GameUiState.ActiveGame(Game(), Board.getValidMoves(Board(), Player.BLACK))
        )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _toastMessage = Channel<Int>()
    val toastMessage = _toastMessage.receiveAsFlow()

    init {
        restoreActiveGame()
    }

    private fun restoreActiveGame() {
        viewModelScope.launch {
            val savedGame = activeGameRepository.getActiveGame()
            if (savedGame != null && !savedGame.isOver) {
                val validMoves = Board.getValidMoves(savedGame.board, savedGame.currentPlayer)
                _uiState.value = GameUiState.ActiveGame(savedGame, validMoves)
            } else {
                activeGameRepository.clearActiveGame()
                resetGame()
            }
        }
    }

    fun playMove(position: Position) {
        val currentState = _uiState.value
        if (currentState !is GameUiState.ActiveGame) return

        viewModelScope.launch {
            val updatedGame = currentState.game.playMove(position)
            if (updatedGame.isOver) {
                val isFavorited = favoriteGameRepository.isGameFavorited(updatedGame.id)
                _uiState.value = GameUiState.GameOver(updatedGame, updatedGame.winner, isFavorited)
                activeGameRepository.clearActiveGame()
            } else {
                val validMoves = Board.getValidMoves(updatedGame.board, updatedGame.currentPlayer)
                _uiState.value = GameUiState.ActiveGame(updatedGame, validMoves)
            }
        }
    }

    fun markAsFavorite(title: String, opponentName: String) {
        val currentState = _uiState.value
        if (currentState !is GameUiState.GameOver) {
            _uiState.value = GameUiState.Error("Cannot mark an ongoing game as favorite.")
            return
        }

        viewModelScope.launch {
            val gameToFavorite = currentState.game
            try {
                favoriteGameRepository.addFavoriteGame(
                    game = gameToFavorite,
                    title = title,
                    opponentName = opponentName,
                )
                _uiState.value = currentState.copy(isFavoriteMarked = true)
                _toastMessage.send(R.string.game_added_to_favorites)
            } catch (e: Exception) {
                _uiState.value =
                    GameUiState.Error("Failed to mark as favorite: ${e.localizedMessage}")
            }
        }
    }

    fun saveActiveGame() {
        val currentState = _uiState.value
        if (currentState is GameUiState.ActiveGame) {
            viewModelScope.launch { activeGameRepository.saveActiveGame(currentState.game) }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            val newGame = Game()
            val validMoves = Board.getValidMoves(newGame.board, newGame.currentPlayer)
            _uiState.value = GameUiState.ActiveGame(newGame, validMoves)
            activeGameRepository.clearActiveGame()
        }
    }
}
