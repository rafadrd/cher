package pt.isel.cher.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pt.isel.cher.R
import pt.isel.cher.ui.components.ActiveGameContent
import pt.isel.cher.ui.components.AddToFavoritesDialog
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.GameOverContent

@Composable
fun GameView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    onCellClick: (Int, Int) -> Unit,
    onAddToFavorites: (String, String) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddFavoriteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CherTopBar(title = stringResource(R.string.game_title), onNavigateBack = onBack)
        }
    ) {
        Column(
            modifier =
                modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (uiState) {
                is GameUiState.ActiveGame -> {
                    val activeGameState = uiState as GameUiState.ActiveGame
                    ActiveGameContent(
                        game = activeGameState.game,
                        onCellClick = onCellClick,
                        onResetGame = { viewModel.resetGame() },
                    )
                }

                is GameUiState.GameOver -> {
                    val gameOverState = uiState as GameUiState.GameOver
                    GameOverContent(
                        game = gameOverState.game,
                        winner = gameOverState.winner,
                        isFavoriteMarked = gameOverState.isFavoriteMarked,
                        onAddToFavorites = { showAddFavoriteDialog = true },
                        onResetGame = { viewModel.resetGame() },
                    )
                }

                is GameUiState.Error -> {
                    val errorMessage = (uiState as GameUiState.Error).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            if (showAddFavoriteDialog && uiState is GameUiState.GameOver) {
                AddToFavoritesDialog(
                    onConfirm = { title, opponentName ->
                        onAddToFavorites(title, opponentName)
                        showAddFavoriteDialog = false
                    },
                    onDismiss = { showAddFavoriteDialog = false },
                )
            }
        }
    }
}
