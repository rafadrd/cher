package pt.isel.cher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Player
import pt.isel.cher.ui.theme.CheRTheme
import pt.isel.cher.ui.theme.cellSize
import pt.isel.cher.ui.theme.pieceSize

@Composable
fun BoardGrid(
    modifier: Modifier = Modifier,
    board: Board,
    onCellClick: (Int, Int) -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier =
            modifier
                .horizontalScroll(rememberScrollState())
                .background(Color(0xFF0A8031))
                .padding(8.dp)
                .border(4.dp, Color.Black)
    ) {
        Column {
            board.grid.forEachIndexed { rowIndex, row ->
                Row {
                    row.forEachIndexed { colIndex, cell ->
                        Box(
                            modifier =
                                Modifier.Companion.size(cellSize)
                                    .background(Color(0xFF004000))
                                    .border(1.dp, Color.Black)
                                    .clickable(enabled = enabled && cell == null) {
                                        onCellClick(rowIndex, colIndex)
                                    },
                            contentAlignment = Alignment.Center,
                        ) {
                            when (cell) {
                                Player.BLACK ->
                                    Image(
                                        painter = painterResource(id = R.drawable.black_piece),
                                        contentDescription =
                                            stringResource(R.string.black_piece_desc),
                                        modifier = Modifier.Companion.size(pieceSize),
                                        contentScale = ContentScale.Fit,
                                    )

                                Player.WHITE ->
                                    Image(
                                        painter = painterResource(id = R.drawable.white_piece),
                                        contentDescription =
                                            stringResource(R.string.white_piece_desc),
                                        modifier = Modifier.Companion.size(pieceSize),
                                        contentScale = ContentScale.Fit,
                                    )

                                null -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardGridPreview() {
    CheRTheme { BoardGrid(board = Board(), onCellClick = { _, _ -> }, enabled = true) }
}
