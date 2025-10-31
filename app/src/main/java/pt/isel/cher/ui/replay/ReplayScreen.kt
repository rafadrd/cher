package pt.isel.cher.ui.replay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pt.isel.cher.R
import pt.isel.cher.domain.FavoriteGame
import pt.isel.cher.domain.FavoriteInfo
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import pt.isel.cher.ui.components.BoardGrid
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.ErrorState
import pt.isel.cher.ui.components.LoadingState
import pt.isel.cher.ui.components.ReplayControls
import pt.isel.cher.ui.components.ReplayHeader
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun ReplayScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val viewModel: ReplayViewModel = hiltViewModel()
    ReplayScreenContent(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value,
        onNextMove = { viewModel.goToNextMove() },
        onPreviousMove = { viewModel.goToPreviousMove() },
        onBack = onBack,
    )
}

@Composable
private fun ReplayScreenContent(
    modifier: Modifier = Modifier,
    uiState: ReplayUiState,
    onNextMove: () -> Unit,
    onPreviousMove: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CherTopBar(title = stringResource(R.string.game_replay_title), onNavigateBack = onBack)

        Spacer(Modifier.height(16.dp))

        when (val state = uiState) {
            is ReplayUiState.Loading -> {
                LoadingState()
            }
            is ReplayUiState.Error -> {
                ErrorState(message = state.message)
            }
            is ReplayUiState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ReplayHeader(
                        currentMove = state.currentMoveIndex,
                        totalMoves = state.totalMoves,
                        currentPlayer = state.currentPlayer,
                    )

                    Spacer(Modifier.height(16.dp))

                    BoardGrid(
                        board = state.currentBoard,
                        onCellClick = { _, _ -> },
                        validMoves = emptySet(),
                        enabled = false,
                    )

                    Spacer(Modifier.height(16.dp))

                    ReplayControls(
                        onPreviousMove = onPreviousMove,
                        onNextMove = onNextMove,
                        isAtStart = state.isAtStart,
                        isAtEnd = state.isAtEnd,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReplayScreenSuccessPreview() {
    val sampleMoves =
        listOf(
            Move(Position(2, 3), Player.BLACK),
            Move(Position(2, 4), Player.WHITE),
            Move(Position(3, 2), Player.BLACK),
        )

    val sampleGame =
        FavoriteGame(
            info = FavoriteInfo("1", "Preview Game", "CPU", System.currentTimeMillis()),
            moves = sampleMoves,
        )

    val finalBoard = sampleGame.replayTo(sampleMoves.size)

    CheRTheme {
        ReplayScreenContent(
            uiState =
                ReplayUiState.Success(
                    favoriteGame = sampleGame,
                    currentBoard = finalBoard,
                    currentMoveIndex = sampleMoves.size,
                ),
            onNextMove = {},
            onPreviousMove = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReplayScreenLoadingPreview() {
    CheRTheme {
        ReplayScreenContent(
            uiState = ReplayUiState.Loading,
            onNextMove = {},
            onPreviousMove = {},
            onBack = {},
        )
    }
}
