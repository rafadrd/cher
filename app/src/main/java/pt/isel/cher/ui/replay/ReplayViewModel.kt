package pt.isel.cher.ui.replay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pt.isel.cher.data.database.relations.FavoriteGameWithMoves
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Position

sealed class ReplayUiState {
    object Loading : ReplayUiState()

    data class Success(
        val favoriteGame: FavoriteGameWithMoves,
        val board: Board,
        val currentMoveIndex: Int,
        val totalMoves: Int,
        val isAtStart: Boolean,
        val isAtEnd: Boolean,
    ) : ReplayUiState()

    data class Error(
        val message: String,
    ) : ReplayUiState()
}

class ReplayViewModel(
    private val favoriteGameRepository: FavoriteGameRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ReplayUiState>(ReplayUiState.Loading)
    val uiState: StateFlow<ReplayUiState> = _uiState.asStateFlow()

    private val gameCache = mutableMapOf<Int, Game>()

    fun loadGame(gameId: String) {
        viewModelScope.launch {
            _uiState.value = ReplayUiState.Loading
            try {
                favoriteGameRepository
                    .getFavoriteGameById(gameId)
                    .firstOrNull()
                    ?.let { favoriteGameWithMoves ->
                        val totalMoves = favoriteGameWithMoves.moves.size
                        val initialGame = Game()
                        gameCache[0] = initialGame

                        _uiState.value =
                            ReplayUiState.Success(
                                favoriteGame = favoriteGameWithMoves,
                                board = initialGame.board,
                                currentMoveIndex = 0,
                                totalMoves = totalMoves,
                                isAtStart = true,
                                isAtEnd = totalMoves == 0,
                            )
                    } ?: run {
                    _uiState.value = ReplayUiState.Error("Game not found.")
                }
            } catch (e: Exception) {
                _uiState.value = ReplayUiState.Error("Failed to load game: ${e.localizedMessage}")
            }
        }
    }

    fun goToNextMove() {
        val currentState = _uiState.value as? ReplayUiState.Success ?: return
        if (currentState.currentMoveIndex >= currentState.totalMoves) return

        val nextMoveIndex = currentState.currentMoveIndex + 1
        val nextMove = currentState.favoriteGame.moves.getOrNull(currentState.currentMoveIndex)
        val updatedGame =
            gameCache[nextMoveIndex] ?: run {
                val previousGame = gameCache[currentState.currentMoveIndex] ?: Game()
                val movedGame =
                    nextMove?.let {
                        previousGame.playMove(Position(it.row, it.col))
                    } ?: previousGame

                gameCache[nextMoveIndex] = movedGame
                movedGame
            }

        _uiState.value =
            currentState.copy(
                board = updatedGame.board,
                currentMoveIndex = nextMoveIndex,
                isAtStart = nextMoveIndex == 0,
                isAtEnd = nextMoveIndex >= currentState.totalMoves,
            )
    }

    fun goToPreviousMove() {
        val currentState = _uiState.value as? ReplayUiState.Success ?: return
        if (currentState.currentMoveIndex <= 0) return

        val previousMoveIndex = currentState.currentMoveIndex - 1
        val updatedGame =
            gameCache[previousMoveIndex] ?: run {
                val reconstructedGame = Game()
                repeat(previousMoveIndex) { i ->
                    val move = currentState.favoriteGame.moves[i]
                    reconstructedGame.playMove(Position(move.row, move.col))
                }
                gameCache[previousMoveIndex] = reconstructedGame
                reconstructedGame
            }

        _uiState.value =
            currentState.copy(
                board = updatedGame.board,
                currentMoveIndex = previousMoveIndex,
                isAtStart = (previousMoveIndex == 0),
                isAtEnd = false,
            )
    }
}
