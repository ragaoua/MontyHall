import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


/**
 * A custom switch with two labels (texts). When toggled, the selection box animates from
 * one side to the other. NB: the moving of the selection box is done by animating the start
 * padding of the selection box (as well as its width, to match the newly selected item's width).
 *
 * @param modifier Modifier to be applied to the switch.
 * @param lText Text to be displayed on the left side of the switch.
 * @param rText Text to be displayed on the right side of the switch.
 * @param onValueChange Callback to be invoked when the selection changes.
 * @param initialSelection The initial selection of the switch.
 * @param backgroundColor The background color of the switch.
 * @param selectionColor The color of the selection box.
 * @param shape The shape of the switch.
 */
@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    lText: String,
    rText: String,
    onValueChange: (LabeledSwitchSelection) -> Unit,
    initialSelection: LabeledSwitchSelection = LabeledSwitchSelection.LEFT,
    backgroundColor: Color = MaterialTheme.colors.surface,
    selectionColor: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.small
) {
    var selection by remember { mutableStateOf(initialSelection) }

    var switchHeight by remember { mutableStateOf(0.dp) }
    var leftItemWidth by remember { mutableStateOf(0.dp) }
    var rightItemWidth by remember { mutableStateOf(0.dp) }

    val selectionBoxWidth by animateDpAsState(
        targetValue = if (selection == LabeledSwitchSelection.LEFT) leftItemWidth
        else rightItemWidth,
        label ="Selection box width"
    )
    val selectionBoxStartPadding by animateDpAsState(
        targetValue = if (selection == LabeledSwitchSelection.LEFT) 0.dp else leftItemWidth,
        label = "Selection box start padding"
    )

    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .onGloballyPositioned {
                switchHeight = with(localDensity) {
                    it.size.height.toDp()
                }
            }
            .clickable {
                selection =
                    if (selection == LabeledSwitchSelection.LEFT) LabeledSwitchSelection.RIGHT
                    else LabeledSwitchSelection.LEFT
                onValueChange(selection)
            }
    ) {
        // Selection box : this is the box that moves when the user clicks on the switch
        // The moving is done by animating the start padding of the box (as well as its width)
        Box(
            modifier = Modifier
                .height(switchHeight)
                .padding(start = selectionBoxStartPadding)
                .width(selectionBoxWidth)
                .clip(shape = shape)
                .background(selectionColor)
        )
        // Switch items : these are the two items that the user can click on
        Row {
            SwitchItem(
                text = lText,
                isSelected = selection == LabeledSwitchSelection.LEFT,
                onGloballyPositioned = {
                    leftItemWidth = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
            )
            SwitchItem(
                text = rText,
                isSelected = selection == LabeledSwitchSelection.RIGHT,
                onGloballyPositioned = {
                    rightItemWidth = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
            )
        }
    }
}

@Composable
private fun SwitchItem(
    text: String,
    isSelected: Boolean = false,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    Text(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                onGloballyPositioned(coordinates)
            }
            .padding(horizontal = 4.dp),
        text = text,
        color = if (isSelected) Color.White else Color.Gray
        // TODO use material theme colors and animate the color
    )
}

enum class LabeledSwitchSelection {
    LEFT, RIGHT
}