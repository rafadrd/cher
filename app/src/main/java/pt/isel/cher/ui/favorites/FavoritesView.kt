package pt.isel.cher.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.FavoriteInfo
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.GameItem

@Composable
fun FavoritesView(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel,
    onGameClick: (FavoriteInfo) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        CherTopBar(title = stringResource(id = R.string.favorites_title), onNavigateBack = onBack)

        Spacer(Modifier.height(8.dp))

        when (uiState) {
            is FavoritesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is FavoritesUiState.Error -> {
                val errorMessage = (uiState as FavoritesUiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            is FavoritesUiState.Success -> {
                val favoriteGames = (uiState as FavoritesUiState.Success).favoriteGames
                if (favoriteGames.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_favorite_games_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                } else {
                    LazyColumn {
                        items(items = favoriteGames, key = { it.id }) { favoriteInfo ->
                            GameItem(
                                favoriteInfo = favoriteInfo,
                                onClick = { onGameClick(favoriteInfo) },
                                onDelete = { viewModel.deleteFavoriteGame(favoriteInfo) },
                            )
                        }
                    }
                }
            }
        }
    }
}
