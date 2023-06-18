package io.github.leeonardoo.so.scheduler.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm

@Composable
fun NavigationRailScaffold(
    onClickFab: () -> Unit,
    selectedAlgorithm: Algorithm,
    onClickItem: (Algorithm) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row {
        NavigationRail(
            modifier = Modifier.widthIn(min = 100.dp),
            header = {
                FloatingActionButton(
                    onClick = onClickFab,
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    content = {
                        Icon(Icons.Outlined.Edit, null)
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Algorithm.values().forEach {
                        NavigationRailItem(
                            modifier = Modifier.then(
                                if (it == Algorithm.PreemptiveStaticPriority) {
                                    Modifier.height(72.dp)
                                } else {
                                    Modifier
                                }
                            ),
                            icon = {
                                Icon(it.icon, null)
                            },
                            label = {
                                Text(
                                    it.abbreviatedName,
                                    textAlign = TextAlign.Center
                                )
                            },
                            selected = selectedAlgorithm == it,
                            onClick = {
                                onClickItem(it)
                            }
                        )
                    }
                }
            }
        )

        content()
    }
}