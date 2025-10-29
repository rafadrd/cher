package pt.isel.cher.domain

enum class Player(val displayName: String) {
    BLACK("Black"),
    WHITE("White");

    val opponent: Player
        get() =
            when (this) {
                BLACK -> WHITE
                WHITE -> BLACK
            }

    override fun toString(): String = displayName
}
