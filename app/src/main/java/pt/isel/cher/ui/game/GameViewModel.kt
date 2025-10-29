package pt.isel.cher.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.cher.data.repository.ActiveGameRepository
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import javax.inject.Inject

sealed class GameUiState {
    data class ActiveGame(val game: Game) : GameUiState()

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
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.ActiveGame(Game()))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        restoreActiveGame()
    }

    private fun restoreActiveGame() {
        viewModelScope.launch {
            val savedGame = activeGameRepository.getActiveGame()
            _uiState.value =
                if (savedGame != null && !savedGame.isOver) {
                    GameUiState.ActiveGame(savedGame)
                } else {
                    GameUiState.ActiveGame(Game())
                }
        }
    }

    fun playMove(position: Position) {
        val currentState = _uiState.value
        if (currentState !is GameUiState.ActiveGame) return

        viewModelScope.launch {
            val updatedGame = currentState.game.playMove(position)
            activeGameRepository.saveActiveGame(updatedGame)
            if (updatedGame.isOver) {
                val isFavorited = favoriteGameRepository.isGameFavorited(updatedGame.id)
                _uiState.value = GameUiState.GameOver(updatedGame, updatedGame.winner, isFavorited)
                activeGameRepository.clearActiveGame()
            } else {
                _uiState.value = GameUiState.ActiveGame(updatedGame)
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
            } catch (e: Exception) {
                _uiState.value =
                    GameUiState.Error("Failed to mark as favorite: ${e.localizedMessage}")
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            val newGame = Game()
            _uiState.value = GameUiState.ActiveGame(newGame)
            activeGameRepository.clearActiveGame()
            activeGameRepository.saveActiveGame(newGame)
        }
    }
}
