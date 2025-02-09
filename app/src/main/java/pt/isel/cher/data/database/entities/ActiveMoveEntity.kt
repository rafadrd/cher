package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "active_moves",
    foreignKeys = [
        ForeignKey(
            entity = ActiveGame::class,
            parentColumns = ["id"],
            childColumns = ["activeGameId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["activeGameId"])],
)
data class ActiveMoveEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activeGameId: Int = 1,
    val moveNumber: Int,
    val row: Int,
    val col: Int,
    val player: String, // "BLACK" or "WHITE"
)
