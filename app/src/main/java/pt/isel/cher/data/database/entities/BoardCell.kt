package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "active_board_cells",
    primaryKeys = ["activeGameId", "row", "col"],
    foreignKeys = [
        ForeignKey(
            entity = ActiveGame::class,
            parentColumns = ["id"],
            childColumns = ["activeGameId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BoardCell(
    val activeGameId: Int,
    val row: Int,
    val col: Int,
    val player: String?, // "BLACK", "WHITE" or null
)
