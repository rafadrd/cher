package pt.isel.cher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun BoardGrid(
    modifier: Modifier = Modifier,
    board: Board,
    onCellClick: (Int, Int) -> Unit,
    validMoves: Set<Position>,
    enabled: Boolean,
) {
    val boardContainerColor = MaterialTheme.colorScheme.surfaceVariant
    val cellColor = MaterialTheme.colorScheme.surface
    val validMoveIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(boardContainerColor)
                .padding(8.dp)
                .border(4.dp, MaterialTheme.colorScheme.onSurface),
        contentAlignment = Alignment.Center,
    ) {
        val cellSize = maxWidth / Board.BOARD_SIZE
        val pieceSize = cellSize * 0.85f
        val validMoveIndicatorRadius = with(density) { (cellSize / 6).toPx() }

        Column {
            board.grid.forEachIndexed { rowIndex, row ->
                Row {
                    row.forEachIndexed { colIndex, cell ->
                        val isMoveValid = validMoves.contains(Position(rowIndex, colIndex))
                        val interactionSource = remember { MutableInteractionSource() }

                        Box(
                            modifier =
                                Modifier.size(cellSize)
                                    .background(cellColor)
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                                    .then(
                                        if (enabled && isMoveValid) {
                                            Modifier.clickable(
                                                interactionSource = interactionSource,
                                                indication = null,
                                            ) {
                                                onCellClick(rowIndex, colIndex)
                                            }
                                        } else Modifier
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (enabled && isMoveValid) {
                                Canvas(modifier = Modifier.matchParentSize()) {
                                    drawCircle(
                                        color = validMoveIndicatorColor,
                                        radius = validMoveIndicatorRadius,
                                    )
                                }
                            }

                            when (cell) {
                                Player.BLACK ->
                                    AnimatedPiece(
                                        player = Player.BLACK,
                                        modifier = Modifier.size(pieceSize),
                                    )
                                Player.WHITE ->
                                    AnimatedPiece(
                                        player = Player.WHITE,
                                        modifier = Modifier.size(pieceSize),
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

@Composable
private fun AnimatedPiece(player: Player, modifier: Modifier = Modifier) {
    var isPlaced by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isPlaced = true }

    val scale by
        animateFloatAsState(targetValue = if (isPlaced) 1f else 0f, label = "PieceScaleAnimation")

    val (painterRes, descRes) =
        when (player) {
            Player.BLACK -> Pair(R.drawable.black_piece, R.string.black_piece_desc)
            Player.WHITE -> Pair(R.drawable.white_piece, R.string.white_piece_desc)
        }

    Image(
        painter = painterResource(id = painterRes),
        contentDescription = stringResource(descRes),
        modifier =
            modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentScale = ContentScale.Fit,
    )
}

@Preview(showBackground = true)
@Composable
fun BoardGridPreview() {
    CheRTheme {
        BoardGrid(
            board = Board(),
            onCellClick = { _, _ -> },
            validMoves = setOf(Position(2, 3), Position(3, 2)),
            enabled = true,
        )
    }
}
