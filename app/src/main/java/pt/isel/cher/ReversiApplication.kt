package pt.isel.cher

import android.app.Application
import pt.isel.cher.data.database.CheRDatabase
import pt.isel.cher.data.repository.ActiveGameRepository
import pt.isel.cher.data.repository.FavoriteGameRepository

class ReversiApplication : Application() {
    val database: CheRDatabase by lazy {
        CheRDatabase.getDatabase(this)
    }

    val favoriteGameRepository: FavoriteGameRepository by lazy {
        FavoriteGameRepository(database.favoriteGameDao())
    }

    val activeGameRepository by lazy {
        ActiveGameRepository(
            database.activeGameDao(),
            database.activeMoveDao(),
            database.boardCellDao(),
        )
    }
}
