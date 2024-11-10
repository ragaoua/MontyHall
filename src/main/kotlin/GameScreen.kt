import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun GameScreen() {
    var strategy by remember {
        mutableStateOf(Strategy.KEEP)
    }

    var doors by remember {
        mutableStateOf(randomizeDoors())
    }

    var attempts by remember {
        mutableStateOf(0)
    }
    var winningAttempts by remember {
        mutableStateOf(0)
    }

    var agentDelay: Long? by remember {
        mutableStateOf(null)
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if(doors.none { it.isSelected }) {
                        "Step 1 : Choose a door"
                    } else "Step 2 : Change ?",
                    style = MaterialTheme.typography.h4
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    doors.forEachIndexed { index, door ->
                        Door(
                            door = door,
                            doorNb = index + 1,
                            onClick = {
                                if (doors.none { it.isSelected }) {
                                    val winningOrRandomUnselectedDoor =
                                        doors.filterNot { it == door }.let { unselectedDoors ->
                                            unselectedDoors.find { it.isWinning }
                                                ?: unselectedDoors.random()
                                        }
                                    doors = doors.map {
                                        if (it != door && it != winningOrRandomUnselectedDoor) {
                                            it.copy(isOpen = true)
                                        } else it
                                    }
                                } else {
                                    doors = doors.map {
                                        it.copy(
                                            isOpen = true,
                                            isSelected = it == door
                                        )
                                    }
                                    attempts++
                                    if (door.isWinning) {
                                        winningAttempts++
                                    }
                                }
                                doors = doors.map {
                                    if (it == door) {
                                        it.copy(isSelected = true)
                                    } else it
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Score : $winningAttempts/$attempts",
                    style = MaterialTheme.typography.h6
                )

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {
                        doors = randomizeDoors()
                    }) {
                        Text("Retry")
                    }

                    Button(onClick = {
                        attempts = 0
                        winningAttempts = 0
                        doors = randomizeDoors()
                    }) {
                        Text("Reset")
                    }
                }

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {
                        agentDelay = if (agentDelay == null) {
                            1000L
                        } else null
                    }) {
                        Text(
                            agentDelay?.let { "Stop" } ?: "Start"
                        )
                    }

                    Button(
                        onClick = {
                            agentDelay = agentDelay?.div(2)
                        },
                        enabled = agentDelay?.let{ it > 3 } ?: false
                    ) {
                        Text("Faster")
                    }
                }

                LabeledSwitch(
                    lText = Strategy.KEEP.toString(),
                    rText = Strategy.SWITCH.toString(),
                    onValueChange = {
                        strategy = if (it == LabeledSwitchSelection.LEFT) {
                            Strategy.KEEP
                        } else Strategy.SWITCH
                    }
                )
            }
        }
    }

    LaunchedEffect(agentDelay) {
        agentDelay?.let {
            while (true) {
                delay(it)
                if (doors.none { it.isSelected }) {
                    doors.random().let { selectedDoor ->
                        val winningOrRandomUnselectedDoor =
                            doors.filterNot { it == selectedDoor }.let { unselectedDoors ->
                                unselectedDoors.find { it.isWinning }
                                    ?: unselectedDoors.random()
                            }
                        doors = doors.map {
                            if (it == selectedDoor) {
                                it.copy(isSelected = true)
                            } else if (it != winningOrRandomUnselectedDoor) {
                                it.copy(isOpen = true)
                            } else it
                        }
                    }
                } else if (doors.any { !it.isOpen }) {
                    if (strategy == Strategy.SWITCH) {
                        doors = doors.map {
                            if (!it.isOpen) {
                                it.copy(isSelected = !it.isSelected)
                            } else it
                        }
                    }
                    attempts++
                    if (doors.find { it.isSelected }?.isWinning == true) {
                        winningAttempts++
                    }
                    doors = doors.map {
                        it.copy(isOpen = true)
                    }
                } else {
                    doors = randomizeDoors()
                }
            }
        }
    }
}


@Composable
fun Door(
    door: Door,
    doorNb: Int,
    onClick: () -> Unit
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .conditional(door.isSelected) {
                    border(BorderStroke(
                        width = 4.dp,
                        color = Color.Gray
                    ))
                }
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 200.dp)
                    .background(
                        if(door.isOpen) {
                            if (door.isWinning) {
                                Color.Green
                            } else Color.Red
                        } else Color.Black
                    )
                    .conditional(!door.isOpen) {
                        clickable(onClick = onClick)
                    }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Door $doorNb")
    }
}

fun randomizeDoors(): List<Door> {
    return (1..9)
        .map {
            Door(id = it)
        }
        .toMutableList()
        .apply {
            add(Door(id = 10, isWinning = true))
        }
        .shuffled()
}