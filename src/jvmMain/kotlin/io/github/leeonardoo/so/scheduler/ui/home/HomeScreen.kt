package io.github.leeonardoo.so.scheduler.ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onItemClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulador de Schedulers") },
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
                Text("HomeScreen")
                Button(onItemClick) {
                    Text("Navigate")
                }
            }
        }
    )
}

@Composable
fun ChildScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {

        },
        content = {
            Column {
                Text("ChildScreen")
                Button(onBack) {
                    Text("Navigate back")
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme(darkColorScheme()) {
        HomeScreen()
    }
}