package pt.isel.cher.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isel.cher.ui.about.AboutActivity
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.favorites.FavoritesActivity
import pt.isel.cher.ui.game.GameActivity
import pt.isel.cher.ui.theme.CheRTheme

class MainActivity : BaseActivity() {
    override val tag: String = "MainActivity"

    private val navigateToGameIntent: Intent by lazy {
        Intent(this, GameActivity::class.java)
    }

    private val navigateToFavouritesIntent: Intent by lazy {
        Intent(this, FavoritesActivity::class.java)
    }

    private val navigateToAboutIntent: Intent by lazy {
        Intent(this, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheRTheme {
                MainView(
                    onPlay = { startActivity(navigateToGameIntent) },
                    onFavorites = { startActivity(navigateToFavouritesIntent) },
                    onAbout = { startActivity(navigateToAboutIntent) },
                    onLeave = { finish() },
                )
            }
        }
    }
}
