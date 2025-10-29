package pt.isel.cher.data.repository

import pt.isel.cher.data.database.dao.ActiveGameDao
import pt.isel.cher.data.mapper.toActiveGameEntity
import pt.isel.cher.data.mapper.toDomain
import pt.isel.cher.domain.Game
import javax.inject.Inject

class ActiveGameRepository @Inject constructor(private val activeGameDao: ActiveGameDao) {
    suspend fun getActiveGame(): Game? {
        val activeGameEntity = activeGameDao.getActiveGame() ?: return null
        return activeGameEntity.toDomain()
    }

    suspend fun saveActiveGame(game: Game) {
        val activeGameEntity = game.toActiveGameEntity()
        activeGameDao.saveActiveGame(activeGameEntity)
    }

    suspend fun clearActiveGame() {
        activeGameDao.deleteActiveGame()
    }
}
