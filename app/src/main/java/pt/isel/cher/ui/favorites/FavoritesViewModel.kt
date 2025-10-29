package pt.isel.cher.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pt.isel.cher.data.repository.FavoriteGameRepository
import pt.isel.cher.domain.FavoriteInfo
import javax.inject.Inject

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()

    data class Success(val favoriteGames: List<FavoriteInfo>) : FavoritesUiState()

    data class Error(val message: String) : FavoritesUiState()
}

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(private val favoriteGameRepository: FavoriteGameRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        fetchFavorites()
    }

    fun fetchFavorites() {
        viewModelScope.launch {
            favoriteGameRepository
                .getAllFavoriteGames()
                .onStart { _uiState.value = FavoritesUiState.Loading }
                .catch { e ->
                    _uiState.value =
                        FavoritesUiState.Error("Failed to load favorites: ${e.localizedMessage}")
                }
                .collect { favorites -> _uiState.value = FavoritesUiState.Success(favorites) }
        }
    }

    fun deleteFavoriteGame(favoriteGame: FavoriteInfo) {
        viewModelScope.launch {
            try {
                favoriteGameRepository.deleteFavoriteGame(favoriteGame)
            } catch (e: Exception) {
                _uiState.value =
                    FavoritesUiState.Error("Failed to delete favorite game: ${e.localizedMessage}")
            }
        }
    }
}
