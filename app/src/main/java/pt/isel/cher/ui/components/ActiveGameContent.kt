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
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player

@Composable
fun ActiveGameContent(game: Game, onCellClick: (Int, Int) -> Unit, onResetGame: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.current_player_label),
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            CurrentPlayerIndicator(currentPlayer = game.currentPlayer)
        }

        Spacer(modifier = Modifier.height(16.dp))

        BoardGrid(
            board = game.board,
            onCellClick = onCellClick,
            enabled = true,
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
            Box(modifier = Modifier.weight(1f)) {}

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
