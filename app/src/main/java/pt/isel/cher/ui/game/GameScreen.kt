package pt.isel.cher.ui.game

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pt.isel.cher.R
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import pt.isel.cher.ui.components.AddToFavoritesDialog
import pt.isel.cher.ui.components.CherButton
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.CurrentPlayerIndicator
import pt.isel.cher.ui.components.GameScreenLayout
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun GameScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val viewModel: GameViewModel = hiltViewModel()
    val context = LocalContext.current

    DisposableEffect(Unit) { onDispose { viewModel.saveActiveGame() } }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { messageResId ->
            Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_SHORT).show()
        }
    }

    GameScreenContent(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value,
        onCellClick = { row, col -> viewModel.playMove(Position(row, col)) },
        onAddToFavorites = { title, opponentName -> viewModel.markAsFavorite(title, opponentName) },
        onResetGame = { viewModel.resetGame() },
        onBack = onBack,
    )
}

@Composable
private fun GameScreenContent(
    modifier: Modifier = Modifier,
    uiState: GameUiState,
    onCellClick: (Int, Int) -> Unit,
    onAddToFavorites: (String, String) -> Unit,
    onBack: () -> Unit,
    onResetGame: () -> Unit,
) {
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
                    GameScreenLayout(
                        game = uiState.game,
                        onCellClick = onCellClick,
                        topContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.current_player_label),
                                    style = MaterialTheme.typography.headlineLarge,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                CurrentPlayerIndicator(currentPlayer = uiState.game.currentPlayer)
                            }
                        },
                        bottomContent = {
                            Box(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text = stringResource(R.string.reset_game_button),
                                    onClick = onResetGame,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        },
                    )
                }

                is GameUiState.GameOver -> {
                    GameScreenLayout(
                        game = uiState.game,
                        onCellClick = { _, _ -> },
                        topContent = {
                            Text(
                                text =
                                    uiState.winner?.let {
                                        stringResource(R.string.winner_label, it.toString())
                                    } ?: stringResource(R.string.draw_label),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(16.dp),
                            )
                        },
                        bottomContent = {
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text =
                                        if (uiState.isFavoriteMarked)
                                            stringResource(R.string.favorited_label)
                                        else stringResource(R.string.add_to_favorites_button),
                                    onClick = { showAddFavoriteDialog = true },
                                    enabled = !uiState.isFavoriteMarked,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text = stringResource(R.string.reset_game_button),
                                    onClick = onResetGame,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        },
                    )
                }

                is GameUiState.Error -> {
                    val errorMessage = uiState.message
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

@Preview(showBackground = true)
@Composable
fun GameScreenActivePreview() {
    CheRTheme {
        GameScreenContent(
            uiState = GameUiState.ActiveGame(Game()),
            onCellClick = { _, _ -> },
            onAddToFavorites = { _, _ -> },
            onBack = {},
            onResetGame = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenGameOverPreview() {
    val gameOverGame = Game(isOver = true, winner = Player.BLACK)
    CheRTheme {
        GameScreenContent(
            uiState =
                GameUiState.GameOver(gameOverGame, gameOverGame.winner, isFavoriteMarked = false),
            onCellClick = { _, _ -> },
            onAddToFavorites = { _, _ -> },
            onBack = {},
            onResetGame = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenGameOverFavoritedPreview() {
    val gameOverGame = Game(isOver = true, winner = Player.WHITE)
    CheRTheme {
        GameScreenContent(
            uiState =
                GameUiState.GameOver(gameOverGame, gameOverGame.winner, isFavoriteMarked = true),
            onCellClick = { _, _ -> },
            onAddToFavorites = { _, _ -> },
            onBack = {},
            onResetGame = {},
        )
    }
}
