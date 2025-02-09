package pt.isel.cher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.isel.cher.data.database.dao.ActiveGameDao
import pt.isel.cher.data.database.dao.ActiveMoveDao
import pt.isel.cher.data.database.dao.BoardCellDao
import pt.isel.cher.data.database.dao.FavoriteGameDao
import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.data.database.entities.ActiveMoveEntity
import pt.isel.cher.data.database.entities.BoardCell
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.MoveEntity

@Database(
    entities = [
        FavoriteGame::class,
        MoveEntity::class,
        ActiveGame::class,
        ActiveMoveEntity::class,
        BoardCell::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class CheRDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao

    abstract fun activeGameDao(): ActiveGameDao

    abstract fun activeMoveDao(): ActiveMoveDao

    abstract fun boardCellDao(): BoardCellDao

    companion object {
        @Volatile
        private var instance: CheRDatabase? = null

        fun getDatabase(context: Context): CheRDatabase =
            instance ?: synchronized(this) {
                instance ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        CheRDatabase::class.java,
                        "cher_database",
                    ).fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}
