package pt.isel.cher.data.database.converters

import androidx.room.TypeConverter
import pt.isel.cher.domain.Player

class Converters {
    @TypeConverter
    fun fromPlayer(player: Player?): String? {
        return player?.name
    }

    @TypeConverter
    fun toPlayer(name: String?): Player? {
        return name?.let { Player.valueOf(it) }
    }

    @TypeConverter
    fun fromGrid(grid: List<List<Player?>>): String {
        return grid.joinToString(separator = ";") { row ->
            row.joinToString(separator = ",") { player ->
                when (player) {
                    Player.BLACK -> "B"
                    Player.WHITE -> "W"
                    null -> "N"
                }
            }
        }
    }

    @TypeConverter
    fun toGrid(gridString: String): List<List<Player?>> {
        if (gridString.isEmpty()) return emptyList()
        return gridString.split(";").map { rowString ->
            rowString.split(",").map { playerChar ->
                when (playerChar) {
                    "B" -> Player.BLACK
                    "W" -> Player.WHITE
                    else -> null
                }
            }
        }
    }
}
