package io.github.leeonardoo.so.scheduler.ui.simulation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            SimulationContent(paddingValues = it, algorithm = algorithm)
        }
    )
}

@Composable
private fun SimulationContent(paddingValues: PaddingValues, algorithm: Algorithm) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        val processScrollState = rememberScrollState()
        val contentScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(processScrollState)
                .weight(0.42f)
                .background(Color.Red)
                .fillMaxHeight()
        ) {

        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            modifier = Modifier
                .verticalScroll(contentScrollState)
                .weight(1f)
                .background(Color.Blue)
                .fillMaxHeight()
        ) {

        }
    }
}

@Preview
@Composable
fun SimulationScreenPreview() {
    MaterialTheme(darkColorScheme()) {
        SimulationScreen(algorithm = Algorithm.FIFO)
    }
}