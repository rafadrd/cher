package pt.isel.cher.data.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Player

class Converters {
    @TypeConverter
    fun fromPlayer(player: Player?): String? {
        return player?.dbValue?.toString()
    }

    @TypeConverter
    fun toPlayer(value: String?): Player? {
        if (value == null || value.length != 1) return null
        return Player.fromDbValue(value.first())
    }

    @TypeConverter
    fun fromGrid(grid: List<List<Player?>>): String {
        return Json.encodeToString(grid)
    }

    @TypeConverter
    fun toGrid(gridString: String): List<List<Player?>> {
        if (gridString.isEmpty()) return emptyList()
        return Json.decodeFromString(gridString)
    }

    @TypeConverter
    fun fromMoves(moves: List<Move>): String {
        return Json.encodeToString(moves)
    }

    @TypeConverter
    fun toMoves(movesString: String): List<Move> {
        if (movesString.isEmpty()) return emptyList()
        return Json.decodeFromString(movesString)
    }
}
