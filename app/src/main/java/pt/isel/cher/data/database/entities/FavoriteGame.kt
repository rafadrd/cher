package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "favorite_games")
data class FavoriteGame(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val opponentName: String,
    val dateTime: Long,
)
