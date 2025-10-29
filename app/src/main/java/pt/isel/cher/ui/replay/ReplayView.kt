package pt.isel.cher.ui.replay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.ui.components.BoardGrid
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.ReplayControls
import pt.isel.cher.ui.components.ReplayHeader

@Composable
fun ReplayView(
    viewModel: ReplayViewModel,
    onNextMove: () -> Unit,
    onPreviousMove: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CherTopBar(title = stringResource(R.string.game_replay_title), onNavigateBack = onBack)

        Spacer(Modifier.height(16.dp))

        when (val state = uiState) {
            is ReplayUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ReplayUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            is ReplayUiState.Success -> {
                val currentPlayer =
                    remember(state.currentMoveIndex) {
                        if (state.currentMoveIndex > 0) {
                            val lastMoveIndex = state.currentMoveIndex - 1
                            state.favoriteGame.moves.getOrNull(lastMoveIndex)?.player
                        } else {
                            null
                        }
                    }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ReplayHeader(
                        currentMove = state.currentMoveIndex,
                        totalMoves = state.totalMoves,
                        currentPlayer = currentPlayer,
                    )

                    Spacer(Modifier.height(16.dp))

                    BoardGrid(
                        board = state.currentBoard,
                        onCellClick = { _, _ -> },
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
