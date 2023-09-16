package screens.game

import androidx.compose.foundation.background
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
import screens.core.Strategy
import screens.core.conditional

@Composable
fun GameScreen(
    strategy: Strategy
) {
    var doors by remember {
        mutableStateOf(randomizeDoors())
    }
    var isDoorSelected by remember {
        mutableStateOf(false)
    }

    var attempts by remember {
        mutableStateOf(0)
    }
    var winningAttempts by remember {
        mutableStateOf(0)
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
                    text = if(!isDoorSelected) "Step 1 : Choose a door" else "Step 2 : Change ?",
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
                            doorNb = index+1,
                            onClick = {
                                if(!isDoorSelected) {
                                    isDoorSelected = true
                                    // Open all doors but the one selected and a random other one
                                    val randomUnselectedDoor =
                                        doors.filterNot { it == door }.let { unselectedDoors ->
                                            unselectedDoors.find { it.isWinning }
                                                ?: unselectedDoors.random()
                                        }
                                    doors = doors.map {
                                        if(it != door && it != randomUnselectedDoor) {
                                            it.copy(isOpen = true)
                                        } else it
                                    }
                                } else {
                                    doors = doors.map {
                                        it.copy(isOpen = true)
                                    }
                                    attempts++
                                    if (door.isWinning) {
                                        winningAttempts++
                                    }
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

                Button(
                    onClick = {
                        doors = randomizeDoors()
                        isDoorSelected = false
                    }
                ) {
                    Text("Retry")
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
                .padding(8.dp)
                .conditional(!door.isOpen) {
                    clickable(onClick = onClick)
                },
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
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Door $doorNb")
    }
}

fun randomizeDoors(): List<Door> {
    return (1..2)
        .map {
            Door(id = it, isWinning = false)
        }
        .toMutableList()
        .apply {
            add(Door(id = 10, isWinning = true))
        }
        .shuffled()
}