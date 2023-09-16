package Screens.strategy_selection

data class RadioButtonItem(
    val isSelected: Boolean = false,
    val label: String = "",
    val strategy: Strategy
)