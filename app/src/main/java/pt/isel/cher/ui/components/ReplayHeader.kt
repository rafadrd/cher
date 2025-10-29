package pt.isel.cher.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.cher.R
import pt.isel.cher.domain.Player

@Composable
fun ReplayHeader(currentMove: Int, totalMoves: Int, currentPlayer: Player?) {
    Text(
        text = stringResource(R.string.move_counter, currentMove, totalMoves),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Text(
        text =
            currentPlayer?.let { stringResource(R.string.current_player, it.toString()) }
                ?: stringResource(R.string.game_start),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
