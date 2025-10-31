package pt.isel.cher.data.database.converters

import androidx.room.TypeConverter
import pt.isel.cher.domain.Player

class Converters {
    private val ROW_DELIMITER = ";"
    private val COL_DELIMITER = ","
    private val NULL_PLAYER = "N"

    @TypeConverter
    fun fromPlayer(player: Player?): String? {
        return player?.dbValue?.toString()
    }

    @TypeConverter
    fun toPlayer(name: String?): Player? {
        return name?.firstOrNull()?.let { Player.fromDbValue(it) }
    }

    @TypeConverter
    fun fromGrid(grid: List<List<Player?>>): String {
        return grid.joinToString(separator = ROW_DELIMITER) { row ->
            row.joinToString(separator = COL_DELIMITER) { player ->
                when (player) {
                    Player.BLACK -> Player.BLACK.dbValue.toString()
                    Player.WHITE -> Player.WHITE.dbValue.toString()
                    null -> NULL_PLAYER
                }
            }
        }
    }

    @TypeConverter
    fun toGrid(gridString: String): List<List<Player?>> {
        if (gridString.isEmpty()) return emptyList()
        return gridString.split(ROW_DELIMITER).map { rowString ->
            rowString.split(COL_DELIMITER).map { playerChar ->
                when (playerChar) {
                    Player.BLACK.dbValue.toString() -> Player.BLACK
                    Player.WHITE.dbValue.toString() -> Player.WHITE
                    else -> null
                }
            }
        }
    }
}
