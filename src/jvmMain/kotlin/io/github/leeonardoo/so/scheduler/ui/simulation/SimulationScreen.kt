package io.github.leeonardoo.so.scheduler.ui.simulation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm

@Composable
fun SimulationScreen(algorithm: Algorithm) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(algorithm.title)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                Text("SimulationScreen")
            }
        }
    )
}

@Preview
@Composable
fun SimulationScreen() {
    MaterialTheme(darkColorScheme()) {
        SimulationScreen()
    }
}