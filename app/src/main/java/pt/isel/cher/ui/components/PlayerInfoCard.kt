package pt.isel.cher.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun PlayerInfoCard(
    modifier: Modifier = Modifier,
    player: Player,
    score: Int,
    isCurrentPlayer: Boolean,
) {
    val borderColor by
        animateColorAsState(
            targetValue =
                if (isCurrentPlayer) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            label = "PlayerInfoBorderColor",
        )

    Card(
        modifier =
            modifier.border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            val resources =
                if (player == Player.BLACK) {
                    Pair(R.drawable.black_piece, R.string.piece_black_description)
                } else {
                    Pair(R.drawable.white_piece, R.string.piece_white_description)
                }

            Image(
                painter = painterResource(id = resources.first),
                contentDescription = stringResource(id = resources.second),
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun PlayerInfoCardPreview() {
    CheRTheme {
        Row {
            PlayerInfoCard(player = Player.BLACK, score = 2, isCurrentPlayer = true)
            Spacer(Modifier.width(16.dp))
            PlayerInfoCard(player = Player.WHITE, score = 2, isCurrentPlayer = false)
        }
    }
}
