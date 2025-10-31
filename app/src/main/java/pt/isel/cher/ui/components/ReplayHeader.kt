package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.theme.CheRTheme
import pt.isel.cher.ui.util.toDisplayString

@Composable
fun ReplayHeader(currentMove: Int, totalMoves: Int, currentPlayer: Player?) {
    Text(
        text = stringResource(R.string.replay_header_move_counter, currentMove, totalMoves),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Text(
        text =
            currentPlayer?.let {
                stringResource(R.string.replay_header_current_player, it.toDisplayString())
            } ?: stringResource(R.string.replay_header_game_start),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Preview(showBackground = true)
@Composable
fun ReplayHeaderPreview() {
    CheRTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ReplayHeader(currentMove = 0, totalMoves = 60, currentPlayer = null)
            ReplayHeader(currentMove = 25, totalMoves = 60, currentPlayer = Player.BLACK)
        }
    }
}
