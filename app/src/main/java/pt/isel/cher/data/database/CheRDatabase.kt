package pt.isel.cher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pt.isel.cher.data.database.converters.Converters
import pt.isel.cher.data.database.dao.ActiveGameDao
import pt.isel.cher.data.database.dao.FavoriteGameDao
import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.FavoriteMove

@Database(entities = [FavoriteGame::class, FavoriteMove::class, ActiveGame::class], version = 7)
@TypeConverters(Converters::class)
abstract class CheRDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao

    abstract fun activeGameDao(): ActiveGameDao
}
