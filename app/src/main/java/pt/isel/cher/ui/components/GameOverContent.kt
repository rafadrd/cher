package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun GameOverContent(
    game: Game,
    winner: Player?,
    isFavoriteMarked: Boolean,
    onAddToFavorites: () -> Unit,
    onResetGame: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text(
            text =
                winner?.let { stringResource(R.string.winner_label, it.toString()) }
                    ?: stringResource(R.string.draw_label),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoardGrid(
            board = game.board,
            onCellClick = { _, _ -> },
            enabled = false,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScoreBoard(
            blackScore = game.board.score(Player.BLACK),
            whiteScore = game.board.score(Player.WHITE),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CherButton(
                    text =
                        if (isFavoriteMarked) {
                            stringResource(R.string.favorited_label)
                        } else {
                            stringResource(R.string.add_to_favorites_button)
                        },
                    onClick = onAddToFavorites,
                    enabled = !isFavoriteMarked,
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverContentWinnerPreview() {
    val gameOverGame = Game(isOver = true, winner = Player.BLACK)
    CheRTheme {
        GameOverContent(
            game = gameOverGame,
            winner = gameOverGame.winner,
            isFavoriteMarked = false,
            onAddToFavorites = {},
            onResetGame = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverContentDrawPreview() {
    val gameOverGame = Game(isOver = true, winner = null)
    CheRTheme {
        GameOverContent(
            game = gameOverGame,
            winner = null,
            isFavoriteMarked = true,
            onAddToFavorites = {},
            onResetGame = {},
        )
    }
}
