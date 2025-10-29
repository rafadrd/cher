package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R

@Composable
fun AddToFavoritesDialog(onConfirm: (String, String) -> Unit, onDismiss: () -> Unit) {
    var gameTitle by remember { mutableStateOf("") }
    var opponentName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_to_favorites_dialog_title)) },
        text = {
            Column {
                TextField(
                    value = gameTitle,
                    onValueChange = { gameTitle = it },
                    label = { Text(stringResource(R.string.game_title_label)) },
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = opponentName,
                    onValueChange = { opponentName = it },
                    label = { Text(stringResource(R.string.opponent_name_label)) },
                )
            }
        },
        dismissButton = {
            CherButton(onClick = onDismiss, text = stringResource(R.string.cancel_button))
        },
        confirmButton = {
            CherButton(
                onClick = {
                    if (gameTitle.isNotEmpty() && opponentName.isNotEmpty()) {
                        onConfirm(gameTitle, opponentName)
                    }
                },
                text = stringResource(R.string.save_button),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AddToFavoritesDialogPreview() {
    AddToFavoritesDialog(onConfirm = { _, _ -> }, onDismiss = {})
}
