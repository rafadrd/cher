package pt.isel.cher.ui.replay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.ReversiApplication
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.common.ViewModelFactory
import pt.isel.cher.ui.favorites.FavoritesActivity
import pt.isel.cher.ui.theme.CheRTheme

class ReplayActivity : BaseActivity() {
    override val tag: String = "ReplayActivity"

    private val viewModel: ReplayViewModel by viewModels(
        factoryProducer = {
            ViewModelFactory(
                (application as ReversiApplication).favoriteGameRepository,
                (application as ReversiApplication).activeGameRepository,
            )
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameId = intent.getStringExtra(FavoritesActivity.EXTRA_GAME_ID).orEmpty()
        if (gameId.isBlank()) {
            Toast.makeText(this, "Invalid game data.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.loadGame(gameId)

        setContent {
            CheRTheme {
                ReplayView(
                    viewModel = viewModel,
                    onNextMove = { viewModel.goToNextMove() },
                    onPreviousMove = { viewModel.goToPreviousMove() },
                    onBack = { finish() },
                )
            }
        }
    }
}
