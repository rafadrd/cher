package pt.isel.cher.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.cher.R
import pt.isel.cher.domain.Player

@Composable
fun Player.toDisplayString(): String =
    when (this) {
        Player.BLACK -> stringResource(id = R.string.player_name_black)
        Player.WHITE -> stringResource(id = R.string.player_name_white)
    }
