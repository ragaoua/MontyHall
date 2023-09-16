import Screens.strategy_selection.StrategySelectionScreen
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    StrategySelectionScreen()
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}