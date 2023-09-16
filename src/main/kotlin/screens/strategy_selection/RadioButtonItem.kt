package screens.strategy_selection

import screens.core.Strategy

data class RadioButtonItem(
    val isSelected: Boolean = false,
    val strategy: Strategy
)