package pt.isel.cher.domain

enum class Player(val dbValue: Char) {
    BLACK('B'),
    WHITE('W');

    val opponent: Player
        get() =
            when (this) {
                BLACK -> WHITE
                WHITE -> BLACK
            }

    companion object {
        private val map = entries.associateBy { it.dbValue }

        fun fromDbValue(value: Char): Player? = map[value]
    }
}
