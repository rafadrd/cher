package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun ReplayControls(
    onPreviousMove: () -> Unit,
    onNextMove: () -> Unit,
    isAtStart: Boolean,
    isAtEnd: Boolean,
) {
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CherButton(
            text = stringResource(R.string.replay_controls_previous_button),
            onClick = onPreviousMove,
            enabled = !isAtStart,
        )

        CherButton(
            text = stringResource(R.string.replay_controls_next_button),
            onClick = onNextMove,
            enabled = !isAtEnd,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReplayControlsPreview() {
    CheRTheme {
        Column {
            ReplayControls(onPreviousMove = {}, onNextMove = {}, isAtStart = false, isAtEnd = false)
            ReplayControls(onPreviousMove = {}, onNextMove = {}, isAtStart = true, isAtEnd = false)
            ReplayControls(onPreviousMove = {}, onNextMove = {}, isAtStart = false, isAtEnd = true)
        }
    }
}
