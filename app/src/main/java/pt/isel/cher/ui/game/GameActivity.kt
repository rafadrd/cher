package pt.isel.cher.ui.game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.ReversiApplication
import pt.isel.cher.domain.Position
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.common.ViewModelFactory
import pt.isel.cher.ui.theme.CheRTheme

class GameActivity : BaseActivity() {
    override val tag: String = "GameActivity"

    private val viewModel: GameViewModel by viewModels(
        factoryProducer = {
            ViewModelFactory(
                (application as ReversiApplication).favoriteGameRepository,
                (application as ReversiApplication).activeGameRepository,
            )
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheRTheme {
                GameView(
                    viewModel = viewModel,
                    onCellClick = { row, col -> viewModel.playMove(Position(row, col)) },
                    onAddToFavorites = { title, opponentName ->
                        viewModel.markAsFavorite(title, opponentName)
                        Toast.makeText(this, "Game added to favorites", Toast.LENGTH_SHORT).show()
                    },
                    onBack = { finish() },
                )
            }
        }
    }
}
