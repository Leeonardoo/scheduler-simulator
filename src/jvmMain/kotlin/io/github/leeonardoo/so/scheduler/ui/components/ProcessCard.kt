package io.github.leeonardoo.so.scheduler.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProcessCard(
    modifier: Modifier = Modifier,
    process: SimulatedProcess,
    showPriority: Boolean,
    onClickEdit: () -> Unit = {},
    onClickRemove: () -> Unit = {}
) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf<Offset?>(null) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .onPointerEvent(eventType = PointerEventType.Press) {
                if (it.type == PointerEventType.Press && it.buttons.isSecondaryPressed) {
                    it.changes.forEach { e ->
                        menuOffset = e.position
                        isMenuExpanded = true
                        e.consume()
                    }
                }
            }
    ) {
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            offset = menuOffset?.let { DpOffset(it.x.dp, -it.y.dp) } ?: DpOffset(0.dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text("Editar")
                },
                leadingIcon = {
                    Icon(Icons.Default.Edit, null)
                },
                onClick = {
                    isMenuExpanded = false
                    onClickEdit()
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Remover")
                },
                leadingIcon = {
                    Icon(Icons.Default.Delete, null)
                },
                onClick = {
                    isMenuExpanded = false
                    onClickRemove()
                }
            )
        }

        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(process.color)
            )

            Spacer(Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row {
                    Text("Chegada", modifier = Modifier.weight(1f))

                    Text(process.arrivalTime.toString())
                }

                Row {
                    Text("Burst", modifier = Modifier.weight(1f))

                    Text(process.burstTime.toString())
                }

                if (showPriority) {
                    Row {
                        Text("Prioridade", modifier = Modifier.weight(1f))

                        Text(process.priority.toString())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProcessCardPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.height(80.dp).width(200.dp)) {
            ProcessCard(
                process = SimulatedProcess(
                    color = Color.Green,
                    arrivalTime = 2,
                    burstTime = 1,
                    priority = 2
                ),
                showPriority = false
            )
        }
    }
}

