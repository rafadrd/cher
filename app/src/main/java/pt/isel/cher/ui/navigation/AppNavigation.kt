package pt.isel.cher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.isel.cher.ui.about.AboutScreen
import pt.isel.cher.ui.favorites.FavoritesScreen
import pt.isel.cher.ui.game.GameScreen
import pt.isel.cher.ui.main.MainScreen
import pt.isel.cher.ui.replay.ReplayScreen

object Routes {
    const val MAIN = "main"
    const val GAME = "game"
    const val FAVORITES = "favorites"
    const val REPLAY = "replay/{gameId}"
    const val ABOUT = "about"

    fun replay(gameId: String) = "replay/$gameId"
}

@Composable
fun AppNavigation(onFinish: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN) {
            MainScreen(
                onPlay = { navController.navigate(Routes.GAME) },
                onFavorites = { navController.navigate(Routes.FAVORITES) },
                onAbout = { navController.navigate(Routes.ABOUT) },
                onLeave = onFinish,
            )
        }
        composable(Routes.GAME) { GameScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onGameClick = { navController.navigate(Routes.replay(it.id)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.REPLAY,
            arguments = listOf(navArgument("gameId") { type = NavType.StringType }),
        ) {
            ReplayScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) { AboutScreen(onBack = { navController.popBackStack() }) }
    }
}
