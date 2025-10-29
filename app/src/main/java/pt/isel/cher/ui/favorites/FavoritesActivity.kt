package pt.isel.cher.ui.favorites

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.replay.ReplayActivity
import pt.isel.cher.ui.theme.CheRTheme

class FavoritesActivity : BaseActivity() {
    override val tag: String = "FavoritesActivity"

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheRTheme {
                FavoritesView(
                    viewModel = viewModel,
                    onGameClick = {
                        startActivity(ReplayActivity.Companion.newIntent(this, it.id))
                    },
                    onBack = { finish() },
                )
            }
        }
    }
}
