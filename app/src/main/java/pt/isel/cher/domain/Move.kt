package pt.isel.cher.domain

import kotlinx.serialization.Serializable

@Serializable data class Move(val position: Position, val player: Player)
