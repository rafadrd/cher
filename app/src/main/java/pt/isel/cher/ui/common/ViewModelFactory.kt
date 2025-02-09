package pt.isel.cher.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.cher.data.repository.ActiveGameRepository
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.ui.about.AboutViewModel
import pt.isel.cher.ui.favorites.FavoritesViewModel
import pt.isel.cher.ui.game.GameViewModel
import pt.isel.cher.ui.replay.ReplayViewModel

class ViewModelFactory(
    private val factoryGameRepository: FavoriteGameRepository,
    private val activeGameRepository: ActiveGameRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(factoryGameRepository, activeGameRepository) as T

            modelClass.isAssignableFrom(FavoritesViewModel::class.java) ->
                FavoritesViewModel(factoryGameRepository) as T

            modelClass.isAssignableFrom(ReplayViewModel::class.java) ->
                ReplayViewModel(factoryGameRepository) as T

            modelClass.isAssignableFrom(AboutViewModel::class.java) ->
                AboutViewModel() as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
}
