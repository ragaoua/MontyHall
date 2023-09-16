package Screens.strategy_selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StrategySelectionScreen() {
    val radioButtons = remember {
        mutableStateListOf(
            RadioButtonItem(
                isSelected = true,
                label = "Switch",
                strategy = Strategy.KEEP
            ),
            RadioButtonItem(
                label = "Keep",
                strategy = Strategy.SWITCH
            )
        )
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Text(
                    text = "Monty Hall",
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Select a strategy",
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(8.dp))

                radioButtons.forEach { radioButton ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                radioButtons.replaceAll {
                                    it.copy(
                                        isSelected = it.strategy == radioButton.strategy
                                    )
                                }
                            }
                            .padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = radioButton.isSelected,
                            onClick = {
                                radioButtons.replaceAll {
                                    it.copy(
                                        isSelected = it.strategy == radioButton.strategy
                                    )
                                }
                            }
                        )
                        Text(radioButton.label)
                    }
                }

                Button(
                    onClick = {
                        //radioButtons.find { it.isSelected }?.let {
                        //    it.label
                        //}
                    }
                ) {
                    Text("Start")
                }
            }
        }
    }
}