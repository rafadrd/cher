package pt.isel.cher.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.ReversiApplication
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.common.ViewModelFactory
import pt.isel.cher.ui.replay.ReplayActivity
import pt.isel.cher.ui.theme.CheRTheme

class FavoritesActivity : BaseActivity() {
    override val tag: String = "FavoritesActivity"

    private val viewModel: FavoritesViewModel by viewModels(
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
                FavoritesView(
                    viewModel = viewModel,
                    onGameClick = { startActivity(createNavigateToChannelIntent(it)) },
                    onBack = { finish() },
                )
            }
        }
    }

    private fun createNavigateToChannelIntent(favoriteGame: FavoriteGame): Intent =
        Intent(this, ReplayActivity::class.java).apply {
            putExtra(EXTRA_GAME_ID, favoriteGame.id)
        }

    companion object {
        const val EXTRA_GAME_ID = "gameId"
    }
}
