package screens.game

data class Door(
    val id: Int,
    val isOpen: Boolean = false,
    val isSelected: Boolean = false,
    val isWinning: Boolean = false
)