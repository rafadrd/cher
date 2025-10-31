package pt.isel.cher.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pt.isel.cher.R
import pt.isel.cher.domain.FavoriteInfo
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.components.EmptyState
import pt.isel.cher.ui.components.ErrorState
import pt.isel.cher.ui.components.GameItem
import pt.isel.cher.ui.components.LoadingState
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onGameClick: (FavoriteInfo) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel: FavoritesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    FavoritesScreenContent(
        modifier = modifier,
        uiState = uiState,
        onGameClick = onGameClick,
        onDelete = { viewModel.deleteFavoriteGame(it) },
        onBack = onBack,
    )
}

@Composable
private fun FavoritesScreenContent(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState,
    onGameClick: (FavoriteInfo) -> Unit,
    onDelete: (FavoriteInfo) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        CherTopBar(
            title = stringResource(id = R.string.favorites_screen_title),
            onNavigateBack = onBack,
        )

        Spacer(Modifier.height(8.dp))

        when (uiState) {
            is FavoritesUiState.Loading -> {
                LoadingState()
            }
            is FavoritesUiState.Error -> {
                ErrorState(message = uiState.message)
            }
            is FavoritesUiState.Success -> {
                val favoriteGames = uiState.favoriteGames
                if (favoriteGames.isEmpty()) {
                    EmptyState(
                        message = stringResource(R.string.favorites_empty_state_message),
                        icon = Icons.Default.Inbox,
                    )
                } else {
                    LazyColumn {
                        items(items = favoriteGames, key = { it.id }) { favoriteInfo ->
                            GameItem(
                                favoriteInfo = favoriteInfo,
                                onClick = { onGameClick(favoriteInfo) },
                                onDelete = { onDelete(favoriteInfo) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenSuccessPreview() {
    val favoriteGames =
        listOf(
            FavoriteInfo("1", "Game vs John", "John", System.currentTimeMillis()),
            FavoriteInfo("2", "Epic Match", "Jane", System.currentTimeMillis() - 86400000),
        )
    CheRTheme {
        FavoritesScreenContent(
            uiState = FavoritesUiState.Success(favoriteGames),
            onGameClick = {},
            onDelete = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenEmptyPreview() {
    CheRTheme {
        FavoritesScreenContent(
            uiState = FavoritesUiState.Success(emptyList()),
            onGameClick = {},
            onDelete = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenLoadingPreview() {
    CheRTheme {
        FavoritesScreenContent(
            uiState = FavoritesUiState.Loading,
            onGameClick = {},
            onDelete = {},
            onBack = {},
        )
    }
}
