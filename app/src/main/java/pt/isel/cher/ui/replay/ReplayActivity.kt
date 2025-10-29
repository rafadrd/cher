package pt.isel.cher.ui.replay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.R
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.theme.CheRTheme

class ReplayActivity : BaseActivity() {
    override val tag: String = "ReplayActivity"

    private val viewModel: ReplayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameId = intent.getStringExtra(EXTRA_GAME_ID).orEmpty()
        if (gameId.isBlank()) {
            Toast.makeText(this, getString(R.string.invalid_game_data), Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val EXTRA_GAME_ID = "gameId"

        fun newIntent(context: Context, gameId: String): Intent =
            Intent(context, ReplayActivity::class.java).apply { putExtra(EXTRA_GAME_ID, gameId) }
    }
}
