package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Player

@Composable
fun GameScreenLayout(
    game: Game,
    onCellClick: (Int, Int) -> Unit,
    topContent: @Composable () -> Unit,
    bottomContent: @Composable RowScope.() -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        topContent()

        Spacer(modifier = Modifier.height(16.dp))

        BoardGrid(
            board = game.board,
            onCellClick = onCellClick,
            enabled = !game.isOver,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ScoreBoard(
            blackScore = game.board.score(Player.BLACK),
            whiteScore = game.board.score(Player.WHITE),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomContent()
        }
    }
}
