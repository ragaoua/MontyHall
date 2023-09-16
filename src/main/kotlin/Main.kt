import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import screens.core.Strategy
import screens.game.GameScreen

@Composable
@Preview
fun App() {
    //StrategySelectionScreen()
    GameScreen(strategy = Strategy.KEEP)
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}