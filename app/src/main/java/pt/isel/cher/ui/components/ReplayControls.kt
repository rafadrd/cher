package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.cher.R

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
            text = stringResource(R.string.previous_button),
            onClick = onPreviousMove,
            enabled = !isAtStart,
        )

        CherButton(
            text = stringResource(R.string.next_button),
            onClick = onNextMove,
            enabled = !isAtEnd,
        )
    }
}
