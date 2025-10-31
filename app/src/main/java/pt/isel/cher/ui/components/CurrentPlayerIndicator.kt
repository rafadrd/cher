package pt.isel.cher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.theme.CheRTheme

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
        modifier = Modifier.size(60.dp),
        contentScale = ContentScale.Fit,
    )
}

@Preview(showBackground = true)
@Composable
fun CurrentPlayerIndicatorPreview() {
    CheRTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            CurrentPlayerIndicator(currentPlayer = Player.BLACK)
            CurrentPlayerIndicator(currentPlayer = Player.WHITE)
        }
    }
}