package pt.isel.cher.ui.game

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
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
import pt.isel.cher.ui.components.ErrorState
import pt.isel.cher.ui.components.GameScreenLayout
import pt.isel.cher.ui.components.PlayerInfoCard
import pt.isel.cher.ui.theme.CheRTheme
import pt.isel.cher.ui.util.toDisplayString

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
            CherTopBar(title = stringResource(R.string.game_screen_title), onNavigateBack = onBack)
        }
    ) {
        Column(
            modifier =
                modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (uiState) {
                is GameUiState.ActiveGame -> {
                    val game = uiState.game

                    GameScreenLayout(
                        game = game,
                        onCellClick = onCellClick,
                        validMoves = uiState.validMoves,
                        topContent = {
                            Row(
                                modifier =
                                    Modifier.fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                PlayerInfoCard(
                                    player = Player.BLACK,
                                    score = game.board.score(Player.BLACK),
                                    isCurrentPlayer = game.currentPlayer == Player.BLACK,
                                    modifier = Modifier.weight(1f),
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                PlayerInfoCard(
                                    player = Player.WHITE,
                                    score = game.board.score(Player.WHITE),
                                    isCurrentPlayer = game.currentPlayer == Player.WHITE,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        },
                        bottomContent = {
                            Box(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text = stringResource(R.string.game_button_reset),
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
                        validMoves = emptySet(),
                        topContent = { GameOverContent(winner = uiState.winner) },
                        bottomContent = {
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text =
                                        if (uiState.isFavoriteMarked)
                                            stringResource(R.string.game_button_favorited)
                                        else stringResource(R.string.game_button_add_to_favorites),
                                    onClick = { showAddFavoriteDialog = true },
                                    enabled = !uiState.isFavoriteMarked,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                CherButton(
                                    text = stringResource(R.string.game_button_reset),
                                    onClick = onResetGame,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        },
                    )
                }

                is GameUiState.Error -> {
                    ErrorState(message = uiState.message)
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

@Composable
private fun GameOverContent(winner: Player?) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
                winner?.let { stringResource(R.string.game_gameover_winner, it.toDisplayString()) }
                    ?: stringResource(R.string.game_gameover_draw),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenActivePreview() {
    CheRTheme {
        GameScreenContent(
            uiState = GameUiState.ActiveGame(Game(), emptySet()),
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
