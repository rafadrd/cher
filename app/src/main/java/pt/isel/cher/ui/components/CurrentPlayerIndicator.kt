package pt.isel.cher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import pt.isel.cher.R
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.game.pieceSize

@Composable
fun CurrentPlayerIndicator(currentPlayer: Player) {
    val imageResource =
        if (currentPlayer == Player.BLACK) {
            R.drawable.black_piece
        } else {
            R.drawable.white_piece
        }

    Image(
        painter = painterResource(id = imageResource),
        contentDescription = stringResource(R.string.current_player_indicator),
        modifier = Modifier.Companion.size(pieceSize),
        contentScale = ContentScale.Fit,
    )
}
