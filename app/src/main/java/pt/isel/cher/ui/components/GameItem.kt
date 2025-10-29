package pt.isel.cher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.FavoriteInfo
import java.text.DateFormat
import java.util.Date

@Composable
fun GameItem(
    modifier: Modifier = Modifier,
    favoriteInfo: FavoriteInfo,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Companion.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = favoriteInfo.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.Companion.height(4.dp))
                Text(
                    text = stringResource(id = R.string.opponent_name, favoriteInfo.opponentName),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(4.dp))
                val dateText =
                    remember(favoriteInfo.dateTime) {
                        DateFormat.getDateTimeInstance().format(Date(favoriteInfo.dateTime))
                    }

                Text(
                    text = stringResource(id = R.string.date_played, dateText),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            IconButton(onClick = { onDelete() }) {
                Image(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_favorite_game),
                )
            }
        }
    }
}

@Preview
@Composable
fun GameItemPreview() {
    val favoriteInfo =
        FavoriteInfo(
            id = "10",
            title = "Game Title",
            opponentName = "Opponent Name",
            dateTime = System.currentTimeMillis(),
        )
    GameItem(favoriteInfo = favoriteInfo, onClick = {}, onDelete = {})
}
