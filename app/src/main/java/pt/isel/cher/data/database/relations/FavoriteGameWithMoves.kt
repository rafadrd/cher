package pt.isel.cher.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.MoveEntity

data class FavoriteGameWithMoves(
    @Embedded val favoriteGame: FavoriteGame,
    @Relation(
        parentColumn = "id",
        entityColumn = "favoriteGameId",
    )
    val moves: List<MoveEntity>,
)
