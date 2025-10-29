package pt.isel.cher.domain

data class FavoriteInfo(
    val id: String,
    val title: String,
    val opponentName: String,
    val dateTime: Long,
)

data class FavoriteGame(val info: FavoriteInfo, val moves: List<Move>)
