package pt.isel.cher.ui.replay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.FavoriteGame
import pt.isel.cher.domain.Move
import javax.inject.Inject

sealed class ReplayUiState {
    object Loading : ReplayUiState()

    data class Success(
        val favoriteGame: FavoriteGame,
        val currentBoard: Board,
        val currentMoveIndex: Int,
        val totalMoves: Int,
    ) : ReplayUiState() {
        val isAtStart: Boolean
            get() = currentMoveIndex == 0

        val isAtEnd: Boolean
            get() = currentMoveIndex >= totalMoves
    }

    data class Error(val message: String) : ReplayUiState()
}

@HiltViewModel
class ReplayViewModel
@Inject
constructor(private val favoriteGameRepository: FavoriteGameRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<ReplayUiState>(ReplayUiState.Loading)
    val uiState: StateFlow<ReplayUiState> = _uiState.asStateFlow()

    fun loadGame(gameId: String) {
        viewModelScope.launch {
            _uiState.value = ReplayUiState.Loading
            try {
                favoriteGameRepository.getFavoriteGameById(gameId).firstOrNull()?.let { favoriteGame
                    ->
                    val initialBoard = calculateBoardStateAtIndex(favoriteGame.moves, 0)
                    _uiState.value =
                        ReplayUiState.Success(
                            favoriteGame = favoriteGame,
                            currentBoard = initialBoard,
                            currentMoveIndex = 0,
                            totalMoves = favoriteGame.moves.size,
                        )
                } ?: run { _uiState.value = ReplayUiState.Error("Game not found.") }
            } catch (e: Exception) {
                _uiState.value = ReplayUiState.Error("Failed to load game: ${e.localizedMessage}")
            }
        }
    }

    fun goToNextMove() {
        val currentState = _uiState.value as? ReplayUiState.Success ?: return
        if (currentState.isAtEnd) return

        val nextMoveIndex = currentState.currentMoveIndex + 1
        val nextBoard = calculateBoardStateAtIndex(currentState.favoriteGame.moves, nextMoveIndex)

        _uiState.value =
            currentState.copy(currentMoveIndex = nextMoveIndex, currentBoard = nextBoard)
    }

    fun goToPreviousMove() {
        val currentState = _uiState.value as? ReplayUiState.Success ?: return
        if (currentState.isAtStart) return

        val previousMoveIndex = currentState.currentMoveIndex - 1
        val previousBoard =
            calculateBoardStateAtIndex(currentState.favoriteGame.moves, previousMoveIndex)

        _uiState.value =
            currentState.copy(currentMoveIndex = previousMoveIndex, currentBoard = previousBoard)
    }

    private fun calculateBoardStateAtIndex(moves: List<Move>, index: Int): Board {
        var board = Board()
        for (i in 0 until index) {
            val move = moves[i]
            board = board.playMove(move.position, move.player) ?: board
        }
        return board
    }
}
