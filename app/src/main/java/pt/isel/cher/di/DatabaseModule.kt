package pt.isel.cher.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pt.isel.cher.data.database.CheRDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CheRDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                CheRDatabase::class.java,
                "cher_database",
            )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides fun provideFavoriteGameDao(database: CheRDatabase) = database.favoriteGameDao()

    @Provides fun provideActiveGameDao(database: CheRDatabase) = database.activeGameDao()
}
